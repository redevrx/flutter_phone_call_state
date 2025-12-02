package com.example.flutter_phone_call_state_example

import android.content.Intent
import android.os.Bundle
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle
import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {
    private var hasExecutedForCurrentCall = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FlutterStreamHandle.setOpenAppCallback {
            if (!hasExecutedForCurrentCall) {
                hasExecutedForCurrentCall = true
                bringAppToFront()
            } else {
                println("Callback ignored: already executed for this call")
            }
        }
    }

    private fun bringAppToFront() {
        runOnUiThread {
            if (!isAppOnForeground()) {
                val activityIntent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(activityIntent)
                println("bringAppToFront executed")
            } else {
                println("App already in foreground, skipping bringAppToFront")
            }
        }
    }

    private fun isAppOnForeground(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        return appProcesses.any {
            it.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    it.processName == packageName
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        FlutterStreamHandle.isResumeApp = true
    }

    override fun onStop() {
        super.onStop()
        FlutterStreamHandle.isResumeApp = false
    }

    override fun onDestroy() {
        super.onDestroy()
        FlutterStreamHandle.isResumeApp = false
    }
}
