package week11.st912530.finalproject.data.model

/**
 * Represents the phone's orientation state based on accelerometer data.
 */
sealed class OrientationState {
    /**
     * Phone is facing up (screen visible to user)
     * Z-axis value is approximately +9.8 m/s²
     */
    data class FaceUp(val zValue: Float) : OrientationState()
    
    /**
     * Phone is facing down (screen touching surface)
     * Z-axis value is approximately -9.8 m/s²
     */
    data class FaceDown(val zValue: Float) : OrientationState()
    
    /**
     * Orientation is unclear or in transition
     */
    data class Unknown(val zValue: Float) : OrientationState()
    
    /**
     * Check if orientation is face down
     */
    fun isFaceDown(): Boolean = this is FaceDown
    
    /**
     * Check if orientation is face up
     */
    fun isFaceUp(): Boolean = this is FaceUp
}

/**
 * Constants for orientation detection
 */
object OrientationConstants {
    // Gravity constant (m/s²)
    const val GRAVITY = 9.8f
    
    // Threshold for face down detection (negative Z)
    const val FACE_DOWN_THRESHOLD = -7.0f  // Around -9.8 with tolerance
    
    // Threshold for face up detection (positive Z)
    const val FACE_UP_THRESHOLD = 7.0f  // Around +9.8 with tolerance
    
    // Low-pass filter alpha (0.0 to 1.0, lower = more smoothing)
    const val FILTER_ALPHA = 0.2f
    
    // Debounce time in milliseconds (how long orientation must be stable)
    const val DEBOUNCE_TIME_MS = 2000L  // 2 seconds
    
    // Sensor sampling delay
    const val SENSOR_DELAY_US = 200_000  // 200ms (5 Hz)
}

