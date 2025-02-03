package com.redevrx.flutter_phone_call_state

import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private val flutterHandler =  FlutterStreamHandle

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.init(flutterPluginBinding)
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
