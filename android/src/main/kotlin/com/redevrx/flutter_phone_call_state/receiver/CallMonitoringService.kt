package com.redevrx.flutter_phone_call_state.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle

class CallMonitoringService :Service(){
    companion object {
        private const val CHANNEL_ID = "phone_call_state_channel"
        private const val NOTIFICATION_ID = 1782
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, createNotification())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Phone Call State Monitoring",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for monitoring phone call state"
        }

        // Register the channel with the system
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring Call State")
            .setContentText("Your call state is being monitored.")
//            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FlutterStreamHandle.monitorCall()
        return  START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}