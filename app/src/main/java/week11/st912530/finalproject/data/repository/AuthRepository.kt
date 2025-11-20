package week11.st912530.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st912530.finalproject.data.constants.FirestoreConstants
import week11.st912530.finalproject.data.model.UserProfile

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IAuthRepository {

    override suspend fun signup(first: String, last: String, email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No user ID returned")

        // Use UserProfile data class instead of raw map
        val userProfile = UserProfile(
            firstName = first,
            lastName = last,
            email = email
        )

        // Use constant for collection name
        db.collection(FirestoreConstants.COLLECTION_USERS)
            .document(uid)
            .set(userProfile)
            .await()
        
        return uid
    }

    override suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("No user ID returned")
    }

    override suspend fun getUserProfile(uid: String): Map<String, Any>? {
        val doc = db.collection(FirestoreConstants.COLLECTION_USERS)
            .document(uid)
            .get()
            .await()
        return doc.data
    }

    override fun logout() {
        auth.signOut()
    }

    override fun currentUser(): String? = auth.currentUser?.uid
}