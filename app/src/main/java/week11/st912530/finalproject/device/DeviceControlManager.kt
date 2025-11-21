package week11.st912530.finalproject.device

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log

/**
 * Unified manager for controlling all device features.
 */
class DeviceControlManager(
    context: Context,
    activity: Activity,
    private val audioController: IAudioController = AudioController(context),
    private val screenController: IScreenController = ScreenController(activity),
    private val vibrationController: IVibrationController = VibrationController(context)
) : IDeviceController {
    
    companion object {
        private const val TAG = "DeviceControlManager"
        // Vibration patterns
        private val FACE_DOWN_PATTERN = longArrayOf(0, 100, 50, 100)
        private val FACE_UP_PATTERN = longArrayOf(0, 50)
    }
    
    override fun activateFaceDownMode() {
        Log.d(TAG, "📱 Activating FACE DOWN mode")
        
        // Enable silent mode
        val audioSuccess = audioController.enableSilentMode()
        Log.d(TAG, "🔇 Silent mode: ${if (audioSuccess) "SUCCESS" else "FAILED"}")
        
        // Dim screen
        screenController.dimScreen()
        Log.d(TAG, "🌑 Screen dimmed")
        
        // Vibrate to indicate mode change (if audio was successful)
        if (audioSuccess) {
            vibrationController.vibratePattern(FACE_DOWN_PATTERN)
        } else {
            // If audio failed, just vibrate short to indicate detection but no silent mode
            vibrationController.vibrateShort()
        }
    }
    
    override fun activateFaceUpMode() {
        Log.d(TAG, "📱 Activating FACE UP mode")
        
        // Restore normal audio
        audioController.restoreNormalMode()
        Log.d(TAG, "🔔 Audio restored")
        
        // Restore screen brightness
        screenController.restoreBrightness()
        Log.d(TAG, "☀️ Brightness restored")
        
        // Vibrate to indicate mode change
        vibrationController.vibratePattern(FACE_UP_PATTERN)
    }
    
    override fun hasAllPermissions(): Boolean {
        return audioController.hasPermission()
    }
    
    override fun getMissingPermissions(): List<String> {
        val missing = mutableListOf<String>()
        
        if (!audioController.hasPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                missing.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            }
        }
        
        return missing
    }
}

