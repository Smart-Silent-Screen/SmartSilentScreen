package week11.st912530.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser)
        }

        auth.addAuthStateListener(listener)
        // send initial state
        trySend(auth.currentUser)

        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signup(first: String, last: String, email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No user ID returned")

        val userData = mapOf(
            "firstName" to first,
            "lastName" to last,
            "email" to email
        )

        db.collection("users").document(uid).set(userData).await()
        return uid
    }

    suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("No user ID returned")
    }

    suspend fun getUserProfile(uid: String): Map<String, Any>? {
        val doc = db.collection("users").document(uid).get().await()
        return doc.data
    }

    fun logout() {
        auth.signOut()
    }
}