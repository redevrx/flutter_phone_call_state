package com.redevrx.flutter_phone_call_state

import android.content.Intent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle
import com.redevrx.flutter_phone_call_state.receiver.CallMonitoringService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private val flutterHandler =  FlutterStreamHandle

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.init(flutterPluginBinding)

    runBlocking {
      val context = flutterPluginBinding.applicationContext
      val intent = Intent(context,CallMonitoringService::class.java)
      context.stopService(intent)
      delay(1000)
      context.startService(intent)
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
