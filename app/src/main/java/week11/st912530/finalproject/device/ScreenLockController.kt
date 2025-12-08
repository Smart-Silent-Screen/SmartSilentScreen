package week11.st912530.finalproject.device

import android.app.Activity
import android.content.Context
import android.os.PowerManager

interface IScreenLockController {
    fun releaseWakeLock()
    fun acquireWakeLock()
    fun hasWakeLock(): Boolean
}

class ScreenLockController(private val context: Context) : IScreenLockController {
    
    private val powerManager: PowerManager =
        context.getSystemService(Context.POWER_SERVICE) as PowerManager
    
    private var wakeLock: PowerManager.WakeLock? = null
    
    companion object {
        private const val TAG = "ScreenLockController"
        private const val WAKE_LOCK_TAG = "SmartSilentScreen:ScreenLock"
    }
    
    fun dimScreen(activity: Activity) {
        try {
            activity.window?.attributes = activity.window?.attributes?.apply {
                screenBrightness = 0.01f
            }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    fun restoreBrightness(activity: Activity) {
        try {
            activity.window?.attributes = activity.window?.attributes?.apply {
                screenBrightness = -1f
            }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    override fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) {
            return
        }
        
        try {
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG
            ).apply {
                acquire(10 * 60 * 1000L)
            }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    override fun releaseWakeLock() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            wakeLock = null
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    override fun hasWakeLock(): Boolean {
        return wakeLock?.isHeld == true
    }
}

