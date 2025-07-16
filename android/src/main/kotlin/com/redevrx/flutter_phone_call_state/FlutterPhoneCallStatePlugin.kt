package com.redevrx.flutter_phone_call_state

import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle
import com.redevrx.flutter_phone_call_state.receiver.CallMonitoringService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private val flutterHandler =  FlutterStreamHandle
  private lateinit var methodChannel:MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.init(flutterPluginBinding)
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger,"phone_call_state_monitor")

    val context = flutterPluginBinding.applicationContext
    val intent = Intent(context, CallMonitoringService::class.java)

    methodChannel.setMethodCallHandler { call, result ->
      if (call.method == "startCallService"){
        CoroutineScope(Dispatchers.Default).launch {
          context.stopService(intent)
          delay(1000)

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
          } else {
            context.startService(intent)
          }
        }
        result.success(true)
      }
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
