package week11.st912530.finalproject.sensor

import kotlinx.coroutines.flow.Flow
import week11.st912530.finalproject.data.model.OrientationState

/**
 * Interface for sensor management operations.
 * Following Dependency Inversion Principle - depend on abstractions.
 * Following Interface Segregation Principle - focused interface.
 */
interface ISensorManager {
    
    /**
     * Flow that emits orientation state changes
     */
    val orientationFlow: Flow<OrientationState>
    
    /**
     * Start listening to sensor events
     */
    fun startListening()
    
    /**
     * Stop listening to sensor events
     */
    fun stopListening()
    
    /**
     * Check if sensor is currently active
     */
    fun isListening(): Boolean
    
    /**
     * Check if accelerometer sensor is available
     */
    fun isSensorAvailable(): Boolean
}

