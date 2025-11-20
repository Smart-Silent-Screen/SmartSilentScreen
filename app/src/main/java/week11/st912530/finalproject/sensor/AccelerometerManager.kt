package week11.st912530.finalproject.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import week11.st912530.finalproject.data.model.OrientationConstants
import week11.st912530.finalproject.data.model.OrientationState

/**
 * Manages accelerometer sensor for detecting phone orientation.
 */
class AccelerometerManager(
    private val context: Context
) : ISensorManager, SensorEventListener {
    
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    // Mutable state flow for orientation updates
    private val _orientationFlow = MutableStateFlow<OrientationState>(
        OrientationState.Unknown(0f)
    )
    override val orientationFlow: Flow<OrientationState> = _orientationFlow.asStateFlow()
    
    // Current listening state
    private var listening = false
    
    // Filtered sensor values (for smoothing)
    private var filteredZ = 0f
    
    // Last detected orientation and timestamp for debouncing
    private var lastOrientation: OrientationState = OrientationState.Unknown(0f)
    private var lastOrientationChangeTime = 0L
    
    override fun startListening() {
        if (listening) return
        
        accelerometer?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                OrientationConstants.SENSOR_DELAY_US
            )
            listening = true
        }
    }
    
    override fun stopListening() {
        if (!listening) return
        
        sensorManager.unregisterListener(this)
        listening = false
    }
    
    override fun isListening(): Boolean = listening
    
    override fun isSensorAvailable(): Boolean = accelerometer != null
    
    /**
     * Called when sensor values change.
     * Implements filtering, orientation detection, and debouncing.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
        
        // Get raw Z-axis value (gravity direction)
        val rawZ = event.values[2]
        
        // Apply low-pass filter for smoothing
        filteredZ = applyLowPassFilter(rawZ, filteredZ)
        
        // Detect orientation based on filtered Z value
        val newOrientation = detectOrientation(filteredZ)
        
        // Apply debouncing
        if (shouldUpdateOrientation(newOrientation)) {
            lastOrientation = newOrientation
            lastOrientationChangeTime = System.currentTimeMillis()
            _orientationFlow.value = newOrientation
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required by SensorEventListener
    }
    
    /**
     * Apply low-pass filter to smooth sensor readings.
     * Formula: filtered = alpha * new + (1 - alpha) * old
     */
    private fun applyLowPassFilter(newValue: Float, oldValue: Float): Float {
        return OrientationConstants.FILTER_ALPHA * newValue +
                (1 - OrientationConstants.FILTER_ALPHA) * oldValue
    }
    
    /**
     * Detect orientation based on Z-axis value.
     * Face Down: Z ≈ -9.8 m/s² (negative gravity)
     * Face Up: Z ≈ +9.8 m/s² (positive gravity)
     */
    private fun detectOrientation(zValue: Float): OrientationState {
        return when {
            zValue < OrientationConstants.FACE_DOWN_THRESHOLD -> {
                OrientationState.FaceDown(zValue)
            }
            zValue > OrientationConstants.FACE_UP_THRESHOLD -> {
                OrientationState.FaceUp(zValue)
            }
            else -> {
                OrientationState.Unknown(zValue)
            }
        }
    }
    
    /**
     * Determine if orientation should be updated based on debouncing.
     * Only update if orientation has changed AND enough time has passed.
     */
    private fun shouldUpdateOrientation(newOrientation: OrientationState): Boolean {
        // Check if orientation type has changed
        val orientationChanged = when {
            lastOrientation is OrientationState.FaceDown && newOrientation !is OrientationState.FaceDown -> true
            lastOrientation is OrientationState.FaceUp && newOrientation !is OrientationState.FaceUp -> true
            lastOrientation is OrientationState.Unknown && newOrientation !is OrientationState.Unknown -> true
            else -> false
        }
        
        // If orientation hasn't changed, no update needed
        if (!orientationChanged) return false
        
        // Check if enough time has passed (debouncing)
        val timeSinceLastChange = System.currentTimeMillis() - lastOrientationChangeTime
        return timeSinceLastChange >= OrientationConstants.DEBOUNCE_TIME_MS
    }
}

