package week11.st912530.finalproject.device

/**
 * Unified interface for controlling all device features.
 */
interface IDeviceController {
    
    /**
     * Activate face-down mode (silent + dim screen + vibrate)
     */
    fun activateFaceDownMode()
    
    /**
     * Activate face-up mode (restore audio + restore brightness + vibrate)
     */
    fun activateFaceUpMode()
    
    /**
     * Check if all required permissions are granted
     */
    fun hasAllPermissions(): Boolean
    
    /**
     * Get list of missing permissions
     */
    fun getMissingPermissions(): List<String>
}

