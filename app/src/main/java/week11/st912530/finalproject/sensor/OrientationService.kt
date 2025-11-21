package week11.st912530.finalproject.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrientationService(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    private val _orientationState = MutableStateFlow<OrientationState>(OrientationState.Unknown)
    val orientationState: StateFlow<OrientationState> = _orientationState
    
    private var filteredZ = 0f
    private val alpha = 0.2f
    
    private var lastChangeTime = 0L
    private var tempState: OrientationState = OrientationState.Unknown
    private val debounceDelay = 2000L
    
    fun start() {
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    
    fun stop() {
        sensorManager.unregisterListener(this)
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        
        val z = event.values[2]
        filteredZ = alpha * z + (1 - alpha) * filteredZ
        
        val detectedState = when {
            filteredZ > 7.0f -> OrientationState.FaceUp
            filteredZ < -7.0f -> OrientationState.FaceDown
            else -> OrientationState.Unknown
        }
        
        val currentTime = System.currentTimeMillis()
        
        if (detectedState != tempState) {
            tempState = detectedState
            lastChangeTime = currentTime
        } else if (currentTime - lastChangeTime > debounceDelay) {
            if (detectedState != _orientationState.value) {
                _orientationState.value = detectedState
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

