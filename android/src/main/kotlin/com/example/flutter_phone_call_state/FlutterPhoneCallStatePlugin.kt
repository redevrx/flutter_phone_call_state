package com.example.flutter_phone_call_state

import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.example.flutter_phone_call_state.handle.FlutterHandle

/** FlutterPhoneCallStatePlugin */
class FlutterPhoneCallStatePlugin: FlutterPlugin {
  private lateinit var flutterHandler: FlutterHandle

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler = FlutterHandle(flutterPluginBinding)
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    flutterHandler.dispose()
  }
}
