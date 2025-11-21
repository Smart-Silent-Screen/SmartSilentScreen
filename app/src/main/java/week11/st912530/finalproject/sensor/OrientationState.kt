package week11.st912530.finalproject.sensor

sealed class OrientationState {
    object FaceUp : OrientationState()
    object FaceDown : OrientationState()
    object Unknown : OrientationState()
}

