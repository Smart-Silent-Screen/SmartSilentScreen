package week11.st912530.finalproject.data.repository

import kotlinx.coroutines.flow.Flow
import week11.st912530.finalproject.data.model.EventLog
interface IFirestoreRepository {
    
    /**
     * Log an event to Firestore
     * @param userId User ID who triggered the event
     * @param eventType Type of event (e.g., "FACE_DOWN_START", "FACE_UP")
     * @param duration Duration of the event in milliseconds (optional)
     * @param sessionId Session ID for tracking related events (optional)
     */
    suspend fun logEvent(
        userId: String, 
        eventType: String, 
        duration: Long? = null,
        sessionId: String? = null
    )
    
    /**
     * Get all events for a user
     * @param userId User ID to query events for
     * @return List of event maps
     */
    suspend fun getEvents(userId: String): List<Map<String, Any>>
    
    /**
     * Observe events in real-time using Flow
     * @param userId User ID to observe events for
     * @return Flow of EventLog list that updates in real-time
     */
    fun observeEvents(userId: String): Flow<List<EventLog>>
}

