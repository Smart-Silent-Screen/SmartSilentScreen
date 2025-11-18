package week11.st912530.finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun logEvent(userId: String, eventType: String) {
        val data = mapOf(
            "event" to eventType,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId
        )

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
}
