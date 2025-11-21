package week11.st912530.finalproject.device

import android.app.Activity
import android.view.WindowManager
import java.lang.ref.WeakReference

/**
 * Controls screen brightness.
 * Uses WeakReference to prevent memory leaks.
 */
class ScreenController(activity: Activity) : IScreenController {
    
    private val activityRef = WeakReference(activity)
    private var savedBrightness: Float = -1f
    
    companion object {
        // Minimum brightness (0.15 = 15% - still visible on emulator but clearly dimmed)
        private const val MIN_BRIGHTNESS = 0.15f
        private const val MAX_BRIGHTNESS = 1.0f
    }
    
    override fun dimScreen() {
        activityRef.get()?.let { activity ->
            saveCurrentBrightness()
            
            val window = activity.window
            val layoutParams = window.attributes
            layoutParams.screenBrightness = MIN_BRIGHTNESS
            window.attributes = layoutParams
        }
    }
    
    override fun restoreBrightness() {
        activityRef.get()?.let { activity ->
            val window = activity.window
            val layoutParams = window.attributes
            
            layoutParams.screenBrightness = if (savedBrightness >= 0) {
                savedBrightness
            } else {
                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
            
            window.attributes = layoutParams
        }
    }
    
    override fun saveCurrentBrightness() {
        activityRef.get()?.let { activity ->
            val currentBrightness = activity.window.attributes.screenBrightness
            
            savedBrightness = if (currentBrightness >= 0) {
                currentBrightness
            } else {
                0.5f // Default medium brightness if system default
            }
        }
    }
    
    override fun getCurrentBrightness(): Float {
        return activityRef.get()?.window?.attributes?.screenBrightness ?: -1f
    }
}

