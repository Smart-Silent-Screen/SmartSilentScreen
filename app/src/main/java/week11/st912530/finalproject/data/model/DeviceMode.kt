package week11.st912530.finalproject.data.model

/**
 * Represents the current device mode (silent or normal)
 */
sealed class DeviceMode {
    object Silent : DeviceMode()
    object Normal : DeviceMode()
    fun isSilent(): Boolean = this is Silent

    fun displayName(): String = when (this) {
        is Silent -> "Silent Mode"
        is Normal -> "Normal Mode"
    }
}

/**
 * Represents a sensor session for tracking face-down events
 */
data class SensorSession(
    val sessionId: String,
    val startTime: Long,
    val startOrientation: OrientationState
) {
    //Calculate duration in milliseconds
    fun getDuration(): Long = System.currentTimeMillis() - startTime
}

