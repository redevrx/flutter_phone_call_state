package com.redevrx.flutter_phone_call_state.handle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import com.redevrx.flutter_phone_call_state.receiver.PhoneStateReceiver

class FlutterStreamHandle(binding: FlutterPlugin.FlutterPluginBinding) {
    private var phoneStateEventChannel: EventChannel = EventChannel(binding.binaryMessenger, "flutter_phone_call_state")

    init {
        phoneStateEventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            private lateinit var receiver: PhoneStateReceiver

            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                receiver = object : PhoneStateReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        super.onReceive(context, intent)
                        safeSend(events, mapOf(
                            "status" to status,
                            "phoneNumber" to "${phoneNumber ?: ""}"
                        ))
                    }
                }
                val context = binding.applicationContext
                val hasPhoneStatePermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED

                val hasCallLogPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALL_LOG
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPhoneStatePermission && hasCallLogPermission) {
                    receiver.instance(context)
                    safeSend(events,mapOf(
                        "status" to receiver.status,
                        "phoneNumber" to "${receiver.phoneNumber ?: ""}",
                    ))
                }

                binding.applicationContext.registerReceiver(
                    receiver,
                    IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
                )
            }

            override fun onCancel(arguments: Any?) {
                binding.applicationContext.unregisterReceiver(receiver)
            }
        })
    }

    private fun safeSend(events: EventChannel.EventSink?,data:Map<String,Any>){
        if(data["status"] == 0 || data["status"] == 1){
            if("${data["phoneNumber"]}".length > 6) {
                events?.success(data)
            }
        }else {
            events?.success(data)
        }
    }
    fun dispose() {
        phoneStateEventChannel.setStreamHandler(null)
    }
}