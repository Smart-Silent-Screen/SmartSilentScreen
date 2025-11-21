package week11.st912530.finalproject.device

/**
 * Interface for controlling screen brightness.
 */
interface IScreenController {
    
    /**
     * Dim the screen to minimum brightness
     */
    fun dimScreen()
    
    /**
     * Restore screen to previous brightness
     */
    fun restoreBrightness()
    
    /**
     * Save current brightness level
     */
    fun saveCurrentBrightness()
    
    /**
     * Get current brightness level (0.0 to 1.0)
     */
    fun getCurrentBrightness(): Float
}

