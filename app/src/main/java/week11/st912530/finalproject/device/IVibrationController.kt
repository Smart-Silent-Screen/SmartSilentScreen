package week11.st912530.finalproject.device

/**
 * Interface for controlling device vibration.
 */
interface IVibrationController {
    
    /**
     * Vibrate for a short duration
     */
    fun vibrateShort()
    
    /**
     * Vibrate for a long duration
     */
    fun vibrateLong()
    
    /**
     * Vibrate with a custom pattern
     * @param pattern Array of durations in milliseconds (off, on, off, on, ...)
     */
    fun vibratePattern(pattern: LongArray)
    
    /**
     * Cancel any ongoing vibration
     */
    fun cancel()
}

