package week11.st912530.finalproject.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import week11.st912530.finalproject.MainActivity
import week11.st912530.finalproject.R
import week11.st912530.finalproject.sensor.OrientationService

class OrientationMonitorService : Service() {
    
    private val binder = LocalBinder()
    lateinit var orientationService: OrientationService
        private set
    
    inner class LocalBinder : Binder() {
        fun getService(): OrientationMonitorService = this@OrientationMonitorService
    }
    
    companion object {
        const val CHANNEL_ID = "OrientationMonitorChannel"
        const val NOTIFICATION_ID = 1
    }
    
    override fun onCreate() {
        super.onCreate()
        orientationService = OrientationService(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Smart Silent Screen")
            .setContentText("Monitoring phone orientation")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(createPendingIntent())
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
        orientationService.start()
        
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        orientationService.stop()
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Orientation Monitor",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Monitors phone orientation for silent mode"
        }
        
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    
    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}

