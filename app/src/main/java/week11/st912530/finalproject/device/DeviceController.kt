package week11.st912530.finalproject.device

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository
import java.util.UUID

enum class DeviceMode {
    NORMAL, SILENT
}

class DeviceController(context: Context) {
    
    private val audioController = AudioController(context)
    private val screenController = ScreenLockController(context)
    private val firestoreRepo = FirestoreRepository()
    private val authRepo = AuthRepository()
    private val scope = CoroutineScope(Dispatchers.IO)
    
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    private val _currentMode = MutableStateFlow(DeviceMode.NORMAL)
    val currentMode: StateFlow<DeviceMode> = _currentMode
    
    private var activity: Activity? = null
    private var sessionId: String = ""
    private var sessionStartTime: Long = 0L
    
    fun setActivity(activity: Activity) {
        this.activity = activity
    }
    
    fun activateFaceDownMode() {
        if (_currentMode.value == DeviceMode.SILENT) return
        
        sessionId = UUID.randomUUID().toString()
        sessionStartTime = System.currentTimeMillis()
        
        audioController.saveCurrentMode()
        audioController.enableSilentMode()
        
        activity?.let { screenController.dimScreen(it) }
        
        vibratePattern(longArrayOf(0, 100, 50, 100))
        
        _currentMode.value = DeviceMode.SILENT
        logEvent("FACE_DOWN_START", sessionId = sessionId)
        Log.d("DeviceController", "Face down mode activated - Session: $sessionId")
    }
    
    fun activateFaceUpMode() {
        if (_currentMode.value == DeviceMode.NORMAL) return
        
        val duration = if (sessionStartTime > 0) {
            System.currentTimeMillis() - sessionStartTime
        } else null
        
        audioController.restoreSavedMode()
        
        activity?.let { screenController.restoreBrightness(it) }
        
        vibratePattern(longArrayOf(0, 50))
        
        _currentMode.value = DeviceMode.NORMAL
        
        val currentSessionId = if (sessionId.isNotEmpty()) sessionId else null
        logEvent("FACE_DOWN_END", duration, currentSessionId)
        
        sessionId = ""
        sessionStartTime = 0L
        
        Log.d("DeviceController", "Face up mode activated - Duration: ${duration}ms")
    }
    
    private fun vibratePattern(pattern: LongArray) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
                Log.d("DeviceController", "Vibration triggered: ${pattern.contentToString()}")
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
                Log.d("DeviceController", "Vibration triggered (legacy): ${pattern.contentToString()}")
            }
        } catch (e: Exception) {
            Log.e("DeviceController", "Failed to vibrate: ${e.message}")
        }
    }
    
    private fun logEvent(eventType: String, duration: Long? = null, sessionId: String? = null) {
        authRepo.currentUser()?.let { uid ->
            scope.launch {
                try {
                    firestoreRepo.logEvent(uid, eventType, duration, sessionId)
                } catch (e: Exception) {
                    Log.e("DeviceController", "Failed to log event: ${e.message}")
                }
            }
        }
    }
}

