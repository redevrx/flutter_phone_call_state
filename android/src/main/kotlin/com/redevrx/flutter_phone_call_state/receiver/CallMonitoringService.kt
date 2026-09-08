package com.redevrx.flutter_phone_call_state.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
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
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, createNotification())
        isRunning = true
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
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring Call State")
            .setContentText("Your call state is being monitored.")
            /**
             * A foreground-service notification with no small icon is rejected by the platform:
             * logcat reports "Attempted to start a foreground service with a broken
             * notification" and `startForeground` can throw outright on some versions.
             *
             * A framework drawable is used rather than the host app's launcher icon because a
             * library cannot reference the host's resources, and `R.mipmap.ic_launcher` (which
             * this line used to name, commented out) does not resolve from here at all.
             */
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /**
         * A null [intent] means the system recreated this service on its own after the process
         * died - there is no Flutter engine yet, and without one there is nothing to monitor
         * and nowhere to deliver events. Starting anyway used to throw
         * `UninitializedPropertyAccessException` out of [FlutterStreamHandle.monitorCall] on
         * the main thread, killing the process on the spot; the system then restarted the
         * service and it crashed again. Any other manifest component of the host app - a
         * `PHONE_STATE` receiver, for one - never got to run while that loop was going.
         *
         * So stop instead, and return [START_NOT_STICKY] so the system does not try again.
         * Dart restarts the service through `startCallService` once the engine is back.
         */
        if (!FlutterStreamHandle.monitorCall()) {
            stopSelf(startId)
            return START_NOT_STICKY
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}