package com.redevrx.flutter_phone_call_state

import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.redevrx.flutter_phone_call_state.handle.FlutterStreamHandle

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private lateinit var flutterHandler: FlutterStreamHandle

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler = FlutterStreamHandle(flutterPluginBinding)
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
