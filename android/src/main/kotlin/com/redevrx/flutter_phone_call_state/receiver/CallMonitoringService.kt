import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle

class CallMonitoringService :Service(){

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        val notification = NotificationCompat.Builder(this, "phone_call_state")
            .setContentTitle("Monitor Call")
//            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1782, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FlutterStreamHandle.monitorCall()
        return  START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}