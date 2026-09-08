package com.redevrx.flutter_phone_call_state

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle
import com.redevrx.flutter_phone_call_state.receiver.CallMonitoringService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private companion object {
    private const val TAG = "PhoneCallState"
  }

  private val flutterHandler =  FlutterStreamHandle
  private lateinit var methodChannel:MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.init(flutterPluginBinding)
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger,"phone_call_state_monitor")

    val context = flutterPluginBinding.applicationContext
    val intent = Intent(context, CallMonitoringService::class.java)

    methodChannel.setMethodCallHandler { call, result ->
      if (call.method == "startCallService"){
        startMonitoring(context, intent)
        result.success(true)
      }
    }
  }

  /**
   * Why this is not simply `stopService` + `delay(1000)` + `startForegroundService`.
   *
   * Since Android 12 a foreground service may not be started while the app is in the
   * background, and doing it anyway throws [android.app.ForegroundServiceStartNotAllowedException].
   * The old code slept a second first, from a plain `Dispatchers.Default` coroutine with no
   * exception handler, so the throw landed on a bare coroutine and took the app down.
   *
   * That second was not a rare window - it was the normal case. The host app is opened by a
   * deep link, calls `startCallService` on the way through its splash, and then hands off to
   * the dialer. By the time the delay elapsed the app was backgrounded, and it crashed on
   * essentially every call.
   *
   * So: when the service is already up, re-register the channels directly against the new
   * binding - that is all the stop/start cycle achieved - and touch neither the service nor
   * the delay. When it is not up, start it immediately (the caller is in the foreground at
   * that moment) and treat a refusal as a refusal rather than a crash.
   */
  private fun startMonitoring(context: android.content.Context, intent: Intent) {
    if (CallMonitoringService.isRunning) {
      flutterHandler.monitorCall()
      return
    }

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextCompat.startForegroundService(context, intent)
      } else {
        context.startService(intent)
      }
    } catch (e: Exception) {
      /// Includes ForegroundServiceStartNotAllowedException (an IllegalStateException).
      /// Losing the service degrades the in-process listener; crashing loses the call.
      Log.w(TAG, "could not start CallMonitoringService", e)
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
