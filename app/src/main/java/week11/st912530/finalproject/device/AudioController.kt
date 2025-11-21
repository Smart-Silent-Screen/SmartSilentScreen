package week11.st912530.finalproject.device

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build

/**
 * Controls device audio settings including silent mode.
 */
class AudioController(private val context: Context) : IAudioController {
    
    private val audioManager: AudioManager = 
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    private var savedRingerMode: Int = AudioManager.RINGER_MODE_NORMAL
    
    override fun enableSilentMode(): Boolean {
        if (!hasPermission()) {
            return false
        }
        
        try {
            // Save current ringer mode
            savedRingerMode = audioManager.ringerMode
            
            // Set to silent mode
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            
            return true
        } catch (e: SecurityException) {
            return false
        }
    }
    
    override fun restoreNormalMode(): Boolean {
        if (!hasPermission()) {
            return false
        }
        
        try {
            // Restore saved ringer mode
            audioManager.ringerMode = savedRingerMode
            return true
        } catch (e: SecurityException) {
            return false
        }
    }
    
    override fun isSilentMode(): Boolean {
        return audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT
    }
    
    override fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager.isNotificationPolicyAccessGranted
        } else {
            true
        }
    }
}

