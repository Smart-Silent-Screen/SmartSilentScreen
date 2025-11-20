package week11.st912530.finalproject.data.model

/**
 * User profile data model.
 * Using data class instead of raw maps for type safety.
 */
data class UserProfile(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
) {
    /**
     * Convert to map for Firestore storage
     */
    fun toMap(): Map<String, Any> = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "email" to email
    )
    
    companion object {
        /**
         * Create UserProfile from Firestore map
         */
        fun fromMap(map: Map<String, Any>?): UserProfile? {
            if (map == null) return null
            return UserProfile(
                firstName = map["firstName"]?.toString() ?: "",
                lastName = map["lastName"]?.toString() ?: "",
                email = map["email"]?.toString() ?: ""
            )
        }
    }
}

