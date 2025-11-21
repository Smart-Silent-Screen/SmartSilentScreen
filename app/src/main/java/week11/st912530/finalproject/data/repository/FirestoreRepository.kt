package week11.st912530.finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st912530.finalproject.data.model.EventLog

class FirestoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun logEvent(
        userId: String, 
        eventType: String, 
        duration: Long? = null, 
        sessionId: String? = null
    ) {
        val data = mutableMapOf<String, Any>(
            "event" to eventType,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId
        )
        
        duration?.let { data["duration"] = it }
        sessionId?.let { data["sessionId"] = it }

        db.collection("events")
            .add(data)
            .await()
    }

    suspend fun getEvents(userId: String): List<Map<String, Any>> {
        val snapshot = db.collection("events")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp")
            .get()
            .await()

        return snapshot.documents.map { it.data ?: emptyMap() }
    }

    fun observeEvents(userId: String): Flow<List<EventLog>> = callbackFlow {
        val reg = db.collection("events")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp")
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
