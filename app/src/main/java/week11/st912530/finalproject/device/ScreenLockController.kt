package week11.st912530.finalproject.device

import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.util.Log

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
            Log.d(TAG, "Screen dimmed")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to dim screen: ${e.message}")
        }
    }
    
    fun restoreBrightness(activity: Activity) {
        try {
            activity.window?.attributes = activity.window?.attributes?.apply {
                screenBrightness = -1f
            }
            Log.d(TAG, "Screen brightness restored")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restore brightness: ${e.message}")
        }
    }
    
    override fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) {
            Log.d(TAG, "Wake lock already held")
            return
        }
        
        try {
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG
            ).apply {
                acquire(10 * 60 * 1000L)
            }
            Log.d(TAG, "Wake lock acquired")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to acquire wake lock: ${e.message}")
        }
    }
    
    override fun releaseWakeLock() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                    Log.d(TAG, "Wake lock released")
                }
            }
            wakeLock = null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to release wake lock: ${e.message}")
        }
    }
    
    override fun hasWakeLock(): Boolean {
        return wakeLock?.isHeld == true
    }
}

