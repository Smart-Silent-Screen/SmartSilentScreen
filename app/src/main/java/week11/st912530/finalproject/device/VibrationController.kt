package week11.st912530.finalproject.device

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * Controls device vibration feedback.
 */
class VibrationController(private val context: Context) : IVibrationController {
    
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    companion object {
        private const val TAG = "VibrationController"
        private const val SHORT_VIBRATION_MS = 50L
        private const val LONG_VIBRATION_MS = 200L
    }
    
    override fun vibrateShort() {
        Log.d(TAG, "🔊 Vibrating SHORT (${SHORT_VIBRATION_MS}ms)")
        vibrate(SHORT_VIBRATION_MS)
    }
    
    override fun vibrateLong() {
        Log.d(TAG, "🔊 Vibrating LONG (${LONG_VIBRATION_MS}ms)")
        vibrate(LONG_VIBRATION_MS)
    }
    
    override fun vibratePattern(pattern: LongArray) {
        Log.d(TAG, "🔊 Vibrating PATTERN: ${pattern.contentToString()}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(pattern, -1)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
    
    override fun cancel() {
        Log.d(TAG, "🔇 Vibration cancelled")
        vibrator.cancel()
    }
    
    private fun vibrate(milliseconds: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }
}

