package week11.st912530.finalproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.constants.EventTypes
import week11.st912530.finalproject.data.model.DeviceMode
import week11.st912530.finalproject.data.model.OrientationState
import week11.st912530.finalproject.data.model.SensorSession
import week11.st912530.finalproject.data.repository.IAuthRepository
import week11.st912530.finalproject.data.repository.IFirestoreRepository
import week11.st912530.finalproject.device.IDeviceController
import week11.st912530.finalproject.sensor.ISensorManager
import java.util.UUID

/**
 * ViewModel for managing sensor state and face-down detection.
 */
class SensorViewModel(
    private val sensorManager: ISensorManager,
    private val firestoreRepository: IFirestoreRepository,
    private val authRepository: IAuthRepository,
    private val deviceController: IDeviceController? = null
) : ViewModel() {
    
    // Current orientation state
    var currentOrientation by mutableStateOf<OrientationState>(OrientationState.Unknown(0f))
        private set
    
    // Current device mode (silent/normal)
    var currentMode by mutableStateOf<DeviceMode>(DeviceMode.Normal)
        private set
    
    // Is face-down monitoring enabled?
    var isFaceDownModeEnabled by mutableStateOf(false)
        private set
    
    // Current active session (if face-down)
    var currentSession by mutableStateOf<SensorSession?>(null)
        private set
    
    // Error message
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // Is sensor available?
    var isSensorAvailable by mutableStateOf(false)
        private set
    
    // Permissions status
    var hasRequiredPermissions by mutableStateOf(true)
        private set
    
    var missingPermissions by mutableStateOf<List<String>>(emptyList())
        private set
    
    init {
        checkSensorAvailability()
        checkPermissions()
    }
    
    /**
     * Check device control permissions
     */
    private fun checkPermissions() {
        deviceController?.let {
            hasRequiredPermissions = it.hasAllPermissions()
            missingPermissions = it.getMissingPermissions()
        }
    }
    
    /**
     * Refresh permission status
     */
    fun refreshPermissions() {
        checkPermissions()
    }
    
    /**
     * Check if accelerometer sensor is available
     */
    private fun checkSensorAvailability() {
        isSensorAvailable = sensorManager.isSensorAvailable()
        if (!isSensorAvailable) {
            errorMessage = "Accelerometer sensor not available on this device"
        }
    }
    
    /**
     * Start monitoring face-down/face-up orientation
     */
    fun startMonitoring() {
        if (!isSensorAvailable) {
            errorMessage = "Cannot start: Sensor not available"
            return
        }
        
        if (isFaceDownModeEnabled) return
        
        isFaceDownModeEnabled = true
        sensorManager.startListening()
        
        // Observe orientation changes
        viewModelScope.launch {
            sensorManager.orientationFlow.collect { orientation ->
                handleOrientationChange(orientation)
            }
        }
        
        // Log mode enabled event
        logEvent(EventTypes.MODE_ENABLED)
    }
    
    /**
     * Stop monitoring
     */
    fun stopMonitoring() {
        if (!isFaceDownModeEnabled) return
        
        // End current session if any
        if (currentSession != null) {
            endSession()
        }
        
        isFaceDownModeEnabled = false
        sensorManager.stopListening()
        currentOrientation = OrientationState.Unknown(0f)
        currentMode = DeviceMode.Normal
        
        // Log mode disabled event
        logEvent(EventTypes.MODE_DISABLED)
    }
    
    /**
     * Toggle face-down monitoring on/off
     */
    fun toggleMonitoring() {
        if (isFaceDownModeEnabled) {
            stopMonitoring()
        } else {
            startMonitoring()
        }
    }
    
    /**
     * Handle orientation change from sensor
     */
    private fun handleOrientationChange(newOrientation: OrientationState) {
        val previousOrientation = currentOrientation
        currentOrientation = newOrientation
        
        when {
            // Transitioned to Face Down
            newOrientation.isFaceDown() && !previousOrientation.isFaceDown() -> {
                onFaceDown()
            }
            // Transitioned to Face Up
            newOrientation.isFaceUp() && !previousOrientation.isFaceUp() -> {
                onFaceUp()
            }
        }
    }
    
    /**
     * Handle face-down event
     */
    private fun onFaceDown() {
        // Start new session
        startSession()
        
        // Change mode to silent
        currentMode = DeviceMode.Silent
        
        // Activate device controls (silent mode + dim screen + vibrate)
        deviceController?.activateFaceDownMode()
        
        // Log event
        logEvent(EventTypes.FACE_DOWN_START, sessionId = currentSession?.sessionId)
    }
    
    /**
     * Handle face-up event
     */
    private fun onFaceUp() {
        // End session if exists
        if (currentSession != null) {
            endSession()
        }
        
        // Change mode to normal
        currentMode = DeviceMode.Normal
        
        // Restore device controls (normal audio + restore brightness + vibrate)
        deviceController?.activateFaceUpMode()
        
        // Log event
        logEvent(EventTypes.FACE_UP)
    }
    
    /**
     * Start a new sensor session
     */
    private fun startSession() {
        currentSession = SensorSession(
            sessionId = UUID.randomUUID().toString(),
            startTime = System.currentTimeMillis(),
            startOrientation = currentOrientation
        )
    }
    
    /**
     * End current session and log duration
     */
    private fun endSession() {
        currentSession?.let { session ->
            val duration = session.getDuration()
            logEvent(
                eventType = EventTypes.FACE_DOWN_END,
                duration = duration,
                sessionId = session.sessionId
            )
            currentSession = null
        }
    }
    
    /**
     * Log event to Firestore
     */
    private fun logEvent(
        eventType: String,
        duration: Long? = null,
        sessionId: String? = null
    ) {
        viewModelScope.launch {
            try {
                val userId = authRepository.currentUser()
                if (userId != null) {
                    firestoreRepository.logEvent(
                        userId = userId,
                        eventType = eventType,
                        duration = duration,
                        sessionId = sessionId
                    )
                }
            } catch (e: Exception) {
                errorMessage = "Failed to log event: ${e.message}"
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Cleanup when ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }
}

