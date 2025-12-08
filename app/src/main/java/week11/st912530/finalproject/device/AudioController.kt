package week11.st912530.finalproject.device

import android.content.Context
import android.media.AudioManager

class AudioController(private val context: Context) {
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var savedRingerMode: Int = AudioManager.RINGER_MODE_NORMAL
    
    fun saveCurrentMode() {
        savedRingerMode = audioManager.ringerMode
    }
    
    fun enableSilentMode() {
        try {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    fun restoreSavedMode() {
        try {
            audioManager.ringerMode = savedRingerMode
        } catch (e: Exception) {
            // Silently ignore
        }
    }
}

