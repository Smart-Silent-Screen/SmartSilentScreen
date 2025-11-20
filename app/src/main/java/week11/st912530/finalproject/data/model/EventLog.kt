package week11.st912530.finalproject.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Event log data model for Firestore.
 */
data class EventLog(
    @DocumentId val id: String = "",
    val event: String = "",
    val timestamp: Long = 0L,
    val userId: String = "",
    val duration: Long? = null,  // Duration in milliseconds (for face-down sessions)
    val sessionId: String? = null  // Session ID for tracking related events
) {
    /**
     * Convert to map for Firestore storage
     */
    fun toMap(): Map<String, Any?> = mapOf(
        "event" to event,
        "timestamp" to timestamp,
        "userId" to userId,
        "duration" to duration,
        "sessionId" to sessionId
    )
}
