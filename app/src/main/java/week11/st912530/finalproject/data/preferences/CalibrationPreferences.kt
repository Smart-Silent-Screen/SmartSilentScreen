package week11.st912530.finalproject.data.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages calibration settings for accelerometer thresholds.
 */
class CalibrationPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "calibration_prefs"
        private const val KEY_FACE_DOWN_THRESHOLD = "face_down_threshold"
        private const val KEY_FACE_UP_THRESHOLD = "face_up_threshold"
        private const val KEY_DEBOUNCE_TIME = "debounce_time"
        private const val KEY_FILTER_ALPHA = "filter_alpha"
        
        // Default values
        private const val DEFAULT_FACE_DOWN = -7.0f
        private const val DEFAULT_FACE_UP = 7.0f
        private const val DEFAULT_DEBOUNCE = 2000L
        private const val DEFAULT_ALPHA = 0.2f
    }
    
    var faceDownThreshold: Float
        get() = prefs.getFloat(KEY_FACE_DOWN_THRESHOLD, DEFAULT_FACE_DOWN)
        set(value) = prefs.edit().putFloat(KEY_FACE_DOWN_THRESHOLD, value).apply()
    
    var faceUpThreshold: Float
        get() = prefs.getFloat(KEY_FACE_UP_THRESHOLD, DEFAULT_FACE_UP)
        set(value) = prefs.edit().putFloat(KEY_FACE_UP_THRESHOLD, value).apply()
    
    var debounceTime: Long
        get() = prefs.getLong(KEY_DEBOUNCE_TIME, DEFAULT_DEBOUNCE)
        set(value) = prefs.edit().putLong(KEY_DEBOUNCE_TIME, value).apply()
    
    var filterAlpha: Float
        get() = prefs.getFloat(KEY_FILTER_ALPHA, DEFAULT_ALPHA)
        set(value) = prefs.edit().putFloat(KEY_FILTER_ALPHA, value).apply()
    
    fun resetToDefaults() {
        faceDownThreshold = DEFAULT_FACE_DOWN
        faceUpThreshold = DEFAULT_FACE_UP
        debounceTime = DEFAULT_DEBOUNCE
        filterAlpha = DEFAULT_ALPHA
    }
    
    fun isCalibrated(): Boolean {
        return prefs.contains(KEY_FACE_DOWN_THRESHOLD) ||
                prefs.contains(KEY_FACE_UP_THRESHOLD)
    }
}

