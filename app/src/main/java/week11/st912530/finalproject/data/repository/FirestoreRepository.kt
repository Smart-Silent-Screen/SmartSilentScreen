package week11.st912530.finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st912530.finalproject.data.constants.FirestoreConstants
import week11.st912530.finalproject.data.model.EventLog

class FirestoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IFirestoreRepository {

    override suspend fun logEvent(
        userId: String, 
        eventType: String, 
        duration: Long?,
        sessionId: String?
    ) {
        // Use EventLog data class instead of raw map
        val eventLog = EventLog(
            event = eventType,
            timestamp = System.currentTimeMillis(),
            userId = userId,
            duration = duration,
            sessionId = sessionId
        )

        // Use constant for collection name
        db.collection(FirestoreConstants.COLLECTION_EVENTS)
            .add(eventLog)
            .await()
    }

    override suspend fun getEvents(userId: String): List<Map<String, Any>> {
        val snapshot = db.collection(FirestoreConstants.COLLECTION_EVENTS)
            .whereEqualTo(FirestoreConstants.FIELD_USER_ID, userId)
            .orderBy(FirestoreConstants.FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map { it.data ?: emptyMap() }
    }

    override fun observeEvents(userId: String): Flow<List<EventLog>> = callbackFlow {
        val reg = db.collection(FirestoreConstants.COLLECTION_EVENTS)
            .whereEqualTo(FirestoreConstants.FIELD_USER_ID, userId)
            .orderBy(FirestoreConstants.FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(EventLog::class.java) ?: emptyList()
                trySend(list).isSuccess
            }

        awaitClose { reg.remove() }
    }
}
