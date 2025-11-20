package week11.st912530.finalproject.data.constants

object FirestoreConstants {
    const val COLLECTION_USERS = "users"
    const val COLLECTION_EVENTS = "events"

    const val FIELD_FIRST_NAME = "firstName"
    const val FIELD_LAST_NAME = "lastName"
    const val FIELD_EMAIL = "email"

    const val FIELD_EVENT = "event"
    const val FIELD_TIMESTAMP = "timestamp"
    const val FIELD_USER_ID = "userId"
    const val FIELD_DURATION = "duration"
    const val FIELD_SESSION_ID = "sessionId"
}

object EventTypes {
    const val FACE_DOWN_START = "FACE_DOWN_START"
    const val FACE_DOWN_END = "FACE_DOWN_END"
    const val FACE_UP = "FACE_UP"
    const val MODE_ENABLED = "MODE_ENABLED"
    const val MODE_DISABLED = "MODE_DISABLED"
}

