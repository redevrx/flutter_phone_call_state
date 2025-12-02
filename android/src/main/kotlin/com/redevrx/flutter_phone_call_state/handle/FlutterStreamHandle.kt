package com.redevrx.flutter_phone_call_state.handle

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.CallLog
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.redevrx.flutter_phone_call_state.receiver.PhoneStateReceiver
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface EventCallback {
    fun onStateChange(data: Map<String, Any?>)
}

object FlutterStreamHandle {
    private lateinit var phoneStateEventChannel: EventChannel
    private lateinit var methodChannel: MethodChannel
    private lateinit var methodChannelCallLog: MethodChannel
    private var callback:EventCallback? = null
    private lateinit var mBinding: FlutterPlugin.FlutterPluginBinding
//    private val mainHandler = Handler(Looper.getMainLooper())
     val scope = CoroutineScope(Dispatchers.Main+ SupervisorJob())
    private lateinit var receiver: PhoneStateReceiver
    private var eventSink: EventChannel.EventSink? = null
    var isResumeApp = true
    private var openAppCallback:(()-> Unit)? = null
    private var isEndCall = false

    fun init(binding: FlutterPlugin.FlutterPluginBinding){
        phoneStateEventChannel = EventChannel(binding.binaryMessenger, "flutter_phone_call_state")
        methodChannel = MethodChannel(binding.binaryMessenger,"flutter_phone_call_state_channel")
        methodChannelCallLog = MethodChannel(binding.binaryMessenger,"flutter_phone_call_state_call_log")

        mBinding = binding
    }

    fun setOpenAppCallback(callback:()-> Unit){
        openAppCallback = callback
    }

    fun monitorCall(){
        initCallMethod()
        receiver = object : PhoneStateReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                super.onReceive(context, intent)

                val data = mapOf(
                    "status" to status,
                    "phoneNumber" to (phoneNumber ?: "")
                )
                if(status != 0){
                    isEndCall = true
                }

                scope.launch(Dispatchers.Main){
                    if(!isResumeApp && status == 0 && isEndCall){
                        openAppCallback?.invoke()
                        isEndCall = false
                    }

                    delay(750)
                    safeSend(eventSink, data)
                }
            }
        }

        if(!::phoneStateEventChannel.isInitialized) return
        phoneStateEventChannel.setStreamHandler(object : EventChannel.StreamHandler {

            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                eventSink = events
                val context = mBinding.applicationContext
                val hasPhoneStatePermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED

                val hasCallLogPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALL_LOG
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPhoneStatePermission && hasCallLogPermission) {
                    receiver.setup(mBinding.applicationContext)
                }

                mBinding.applicationContext.registerReceiver(
                    receiver,
                    IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
                )

            }

            override fun onCancel(arguments: Any?) {
                mBinding.applicationContext.unregisterReceiver(receiver)
            }
        })
    }

    private fun initCallMethod(){
        methodChannelCallLog.setMethodCallHandler { call, result ->
            if(call.method == "check_last_call"){
                val isAfterLastCall = call.arguments as? Boolean ?: false
                val data = if(isAfterLastCall) checkAfterLastCall() else checkLastCall()
                result.success(data)
            }
        }
    }

    @SuppressLint("Range")
    private fun checkLastCall(): Map<String, Any> {
        val context = mBinding.applicationContext
        val hasCallLogPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED

        val result = mutableMapOf<String,Any>()

        if(hasCallLogPermission){
            val resolver: ContentResolver = context.contentResolver
            val cursor = resolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE
                ),
                null,
                null,
                "${CallLog.Calls.DATE} DESC"
            )

            cursor?.let {
                if (it.moveToFirst()) {
                    val number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                    val date = it.getLong(it.getColumnIndex(CallLog.Calls.DATE))
                    val duration = it.getInt(it.getColumnIndex(CallLog.Calls.DURATION))
                    val type = it.getInt(it.getColumnIndex(CallLog.Calls.TYPE))

                    val callType = when (type) {
                        CallLog.Calls.INCOMING_TYPE -> "Incoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        else -> "Unknown"
                    }

                    result.putAll(mapOf(
                        "callType" to callType,
                        "number" to number,
                        "date" to date,
                        "duration" to duration,
                        "isAnswer" to (duration > 0))
                    )
                }
            }
            cursor?.close()
        }

        return result
    }

    @SuppressLint("Range")
    private fun checkAfterLastCall(): Map<String, Any> {
        val context = mBinding.applicationContext
        val hasCallLogPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED

        val result = mutableMapOf<String,Any>()

        if(hasCallLogPermission){
            val resolver: ContentResolver = context.contentResolver
            val cursor = resolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE
                ),
                null,
                null,
                "${CallLog.Calls.DATE} DESC"
            )

            cursor?.let {
                if (it.moveToFirst()) {
                    it.moveToNext()

                    if(!it.isAfterLast){
                        val number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                        val date = it.getLong(it.getColumnIndex(CallLog.Calls.DATE))
                        val duration = it.getInt(it.getColumnIndex(CallLog.Calls.DURATION))
                        val type = it.getInt(it.getColumnIndex(CallLog.Calls.TYPE))

                        val callType = when (type) {
                            CallLog.Calls.INCOMING_TYPE -> "Incoming"
                            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                            CallLog.Calls.MISSED_TYPE -> "Missed"
                            else -> "Unknown"
                        }

                        result.putAll(mapOf(
                            "callType" to callType,
                            "number" to number,
                            "date" to date,
                            "duration" to duration,
                            "isAnswer" to (duration > 0))
                        )
                    }
                }
            }
            cursor?.close()
        }

        return result
    }

    private fun safeSend(events: EventChannel.EventSink?, data:Map<String,Any>){
        val mStatus = data["status"]
        if(mStatus == 0 || mStatus == 1){
            if("${data["phoneNumber"]}".length > 6){
                callback?.onStateChange(data)

                events?.success(data)

                methodChannel.invokeMethod("state_change",data)
            }
        } else {
            callback?.onStateChange(data)

            events?.success(data)

            methodChannel.invokeMethod("state_change",data)
        }
    }

    fun setCallback(callback: EventCallback){
        this.callback = callback
    }

    fun dispose() {
        phoneStateEventChannel.setStreamHandler(null)
    }
}