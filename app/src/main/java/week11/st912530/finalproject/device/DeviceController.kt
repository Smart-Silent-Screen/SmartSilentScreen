package week11.st912530.finalproject.device

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository

enum class DeviceMode {
    NORMAL, SILENT
}

class DeviceController(context: Context) {
    
    private val audioController = AudioController(context)
    private val screenController = ScreenLockController(context)
    private val firestoreRepo = FirestoreRepository()
    private val authRepo = AuthRepository()
    private val scope = CoroutineScope(Dispatchers.IO)
    
    private val _currentMode = MutableStateFlow(DeviceMode.NORMAL)
    val currentMode: StateFlow<DeviceMode> = _currentMode
    
    private var activity: Activity? = null
    
    fun setActivity(activity: Activity) {
        this.activity = activity
    }
    
    fun activateFaceDownMode() {
        if (_currentMode.value == DeviceMode.SILENT) return
        
        audioController.saveCurrentMode()
        audioController.enableSilentMode()
        
        activity?.let { screenController.dimScreen(it) }
        
        _currentMode.value = DeviceMode.SILENT
        logEvent("MODE_ENABLED")
        Log.d("DeviceController", "Face down mode activated")
    }
    
    fun activateFaceUpMode() {
        if (_currentMode.value == DeviceMode.NORMAL) return
        
        audioController.restoreSavedMode()
        
        activity?.let { screenController.restoreBrightness(it) }
        
        _currentMode.value = DeviceMode.NORMAL
        logEvent("MODE_DISABLED")
        Log.d("DeviceController", "Face up mode activated")
    }
    
    private fun logEvent(eventType: String) {
        authRepo.currentUser()?.let { uid ->
            scope.launch {
                try {
                    firestoreRepo.logEvent(uid, eventType)
                } catch (e: Exception) {
                    Log.e("DeviceController", "Failed to log event: ${e.message}")
                }
            }
        }
    }
}

