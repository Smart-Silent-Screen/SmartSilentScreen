package week11.st912530.finalproject.utils

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

/**
 * Helper for managing app permissions.
 */
object PermissionHelper {
    
    /**
     * Check if Do Not Disturb access permission is granted
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun hasDoNotDisturbPermission(context: Context): Boolean {
        val notificationManager = 
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.isNotificationPolicyAccessGranted
    }
    
    /**
     * Open system settings to grant Do Not Disturb permission
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestDoNotDisturbPermission(context: Context) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
    
    /**
     * Get user-friendly name for permission
     */
    fun getPermissionDisplayName(permission: String): String {
        return when (permission) {
            Manifest.permission.ACCESS_NOTIFICATION_POLICY -> "Do Not Disturb Access"
            Manifest.permission.VIBRATE -> "Vibrate"
            Manifest.permission.WAKE_LOCK -> "Prevent Sleep"
            else -> permission
        }
    }
    
    /**
     * Get explanation for why permission is needed
     */
    fun getPermissionExplanation(permission: String): String {
        return when (permission) {
            Manifest.permission.ACCESS_NOTIFICATION_POLICY -> 
                "Required to automatically enable silent mode when phone is face-down"
            Manifest.permission.VIBRATE -> 
                "Provides feedback when orientation changes"
            Manifest.permission.WAKE_LOCK -> 
                "Keeps app active to monitor orientation in background"
            else -> "Required for app functionality"
        }
    }
}

