package com.redevrx.flutter_phone_call_state.receiver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle

class CallMonitoringService: Service() {
    @SuppressLint("NewApi")
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val notification: Notification = NotificationCompat.Builder(this, "call_channel")
            .setContentTitle("Call Monitoring Service")
            .setContentText("Monitoring call status in the background")
//            .setSmallIcon(android.R.drawable.ic_dialog_phone)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FlutterStreamHandle.monitorCall()
        return START_STICKY
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "call_channel",
                "Call Monitoring",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}