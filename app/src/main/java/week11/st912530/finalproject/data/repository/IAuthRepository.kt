package week11.st912530.finalproject.data.repository
interface IAuthRepository {
    
    /**
     * Create a new user account
     * @return User ID of the newly created user
     */
    suspend fun signup(first: String, last: String, email: String, password: String): String
    
    /**
     * Login with email and password
     * @return User ID of the logged-in user
     */
    suspend fun login(email: String, password: String): String
    
    /**
     * Get user profile data from Firestore
     * @return Map of user profile data or null if not found
     */
    suspend fun getUserProfile(uid: String): Map<String, Any>?
    
    /**
     * Logout current user
     */
    fun logout()
    
    /**
     * Get current logged-in user ID
     * @return User ID or null if not logged in
     */
    fun currentUser(): String?
}

