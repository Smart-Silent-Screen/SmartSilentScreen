package week11.st912530.finalproject.device

import android.content.Context
import android.media.AudioManager
import android.util.Log

class AudioController(private val context: Context) {
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var savedRingerMode: Int = AudioManager.RINGER_MODE_NORMAL
    
    fun saveCurrentMode() {
        savedRingerMode = audioManager.ringerMode
        Log.d("AudioController", "Saved ringer mode: $savedRingerMode")
    }
    
    fun enableSilentMode() {
        try {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            Log.d("AudioController", "Silent mode enabled")
        } catch (e: Exception) {
            Log.e("AudioController", "Failed to enable silent mode: ${e.message}")
        }
    }
    
    fun restoreSavedMode() {
        try {
            audioManager.ringerMode = savedRingerMode
            Log.d("AudioController", "Restored ringer mode: $savedRingerMode")
        } catch (e: Exception) {
            Log.e("AudioController", "Failed to restore mode: ${e.message}")
        }
    }
}

