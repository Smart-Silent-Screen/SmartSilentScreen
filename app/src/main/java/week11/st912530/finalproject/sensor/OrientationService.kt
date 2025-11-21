package week11.st912530.finalproject.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository
import week11.st912530.finalproject.device.DeviceController

class OrientationService(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    private val _orientationState = MutableStateFlow<OrientationState>(OrientationState.Unknown)
    val orientationState: StateFlow<OrientationState> = _orientationState
    
    private val _isAutomationEnabled = MutableStateFlow(true)
    val isAutomationEnabled: StateFlow<Boolean> = _isAutomationEnabled
    
    val deviceController = DeviceController(context)
    
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
    
    fun enableAutomation() {
        _isAutomationEnabled.value = true
        Log.d("OrientationService", "Automation enabled")
    }
    
    fun disableAutomation() {
        _isAutomationEnabled.value = false
        deviceController.activateFaceUpMode()
        Log.d("OrientationService", "Automation disabled")
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
                handleOrientationChange(detectedState)
            }
        }
    }
    
    private fun handleOrientationChange(state: OrientationState) {
        if (!_isAutomationEnabled.value) return
        
        when (state) {
            OrientationState.FaceDown -> {
                logOrientationEvent("FACE_DOWN_START")
                deviceController.activateFaceDownMode()
            }
            OrientationState.FaceUp -> {
                logOrientationEvent("FACE_UP_START")
                deviceController.activateFaceUpMode()
            }
            OrientationState.Unknown -> {}
        }
    }
    
    private fun logOrientationEvent(eventType: String) {
        val authRepo = AuthRepository()
        val firestoreRepo = FirestoreRepository()
        
        authRepo.currentUser()?.let { uid ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firestoreRepo.logEvent(uid, eventType)
                } catch (e: Exception) {
                    Log.e("OrientationService", "Failed to log event: ${e.message}")
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

