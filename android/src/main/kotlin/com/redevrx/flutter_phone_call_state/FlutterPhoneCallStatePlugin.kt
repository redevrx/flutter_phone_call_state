package com.redevrx.flutter_phone_call_state

import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle
import com.redevrx.flutter_phone_call_state.receiver.CallMonitoringService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private val flutterHandler =  FlutterStreamHandle

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.init(flutterPluginBinding)
    val context = flutterPluginBinding.applicationContext
    val intent = Intent(context, CallMonitoringService::class.java)

    CoroutineScope(Dispatchers.Default).launch {
      delay(2000)
      context.stopService(intent)
      delay(1000)

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextCompat.startForegroundService(context, intent)
      } else {
        context.startService(intent)
      }
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
