package week11.st912530.finalproject.device

/**
 * Interface for controlling device audio settings.
 */
interface IAudioController {
    
    /**
     * Enable silent mode (mute all sounds)
     */
    fun enableSilentMode(): Boolean
    
    /**
     * Restore normal audio mode
     */
    fun restoreNormalMode(): Boolean
    
    /**
     * Check if device is currently in silent mode
     */
    fun isSilentMode(): Boolean
    
    /**
     * Check if app has permission to modify audio settings
     */
    fun hasPermission(): Boolean
}

