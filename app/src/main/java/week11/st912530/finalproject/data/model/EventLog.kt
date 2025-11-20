package week11.st912530.finalproject.data.model

import com.google.firebase.firestore.DocumentId

data class EventLog(
    @DocumentId val id: String = "",
    val event: String = "",
    val timestamp: Long = 0L,
    val userId: String = ""
)