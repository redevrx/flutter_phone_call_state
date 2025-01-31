package com.redevrx.flutter_phone_call_state.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.PhoneStateListener


open class PhoneStateReceiver : BroadcastReceiver() {
    var status: Int = -1
    var phoneNumber: String? = null
    private var isDialing = false
    private var isIncoming = false
    private var lastDialNumber:String? = null
    private var isSecondCallIncoming = false // detect two call

    fun instance(context: Context) {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
        phoneNumber = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val incomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val extraState = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

            extraState.let {
                when (it) {
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        status = 2
                        phoneNumber = incomingNumber
                        isIncoming = true

//                        if (status == 3) {
//                            status = 5
//                        } else {
//                            status = 2
//                        }
//                        phoneNumber = incomingNumber
//                        isIncoming = true
                    }
                    TelephonyManager.EXTRA_STATE_OFFHOOK ->  {
                        if(isIncoming){
                            status = 3
                            isIncoming = false
                        } else if(isDialing){
                            if(incomingNumber != null && incomingNumber != lastDialNumber){
                                status = 4
                                isDialing = false
                            }
                        }else {
                            status = 1
                            isDialing = true
                            lastDialNumber = incomingNumber
                        }

//                        if (isIncoming) {
//                            status = 3
//                            isIncoming = false
//                        } else if (isDialing) {
//                            if (incomingNumber != null && incomingNumber != lastDialNumber) {
//                                status = 4
//                                isDialing = false
//                            }
//                        } else {
//                            status = 1
//                            isDialing = true
//                            lastDialNumber = incomingNumber
//                        }
                    }
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        if(isDialing || isIncoming){
                            status = 0
                            phoneNumber = null
                        }else {
                            ///nothing
                            status = -1
                            phoneNumber = null
                        }

//                        if (isDialing || isIncoming) {
//                            status = 0
//                            phoneNumber = null
//                        } else {
//                            status = -1
//                            phoneNumber = null
//                        }
                    }
                    else -> {
                        status = -1
                    }
                }
            }

            phoneNumber = incomingNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val mPhoneListener = object : PhoneStateListener() {
        @Deprecated("Deprecated in Java")
        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        checkCallStatus(state,incomingNumber)
        }
    }

    private fun checkCallStatus(state:Int,incomingNumber:String?){
        try {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    if(isDialing || isIncoming){
                        status = 0
                        phoneNumber = null
                    }else {
                        ///nothing
                        status = -1
                        phoneNumber = null
                    }
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    status = 2
                    phoneNumber = incomingNumber
                    isIncoming = true

//                    if (status == 3) {
//                        status = 5
//                    } else {
//                        status = 2
//                    }
//                    phoneNumber = incomingNumber
//                    isIncoming = true
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    if(isIncoming){
                        status = 3
                        isIncoming = false
                    } else if(isDialing){
                       if(incomingNumber != null && incomingNumber != lastDialNumber){
                           status = 4
                           isDialing = false
                       }
                    }else {
                        status = 1
                        isDialing = true
                        lastDialNumber = incomingNumber
                    }

//                    if (isSecondCallIncoming) {
//                        status = 6
//                        isSecondCallIncoming = false
//                    } else if (isIncoming) {
//                        status = 3
//                        isIncoming = false
//                    } else if (isDialing) {
//                        if (incomingNumber != null && incomingNumber != lastDialNumber) {
//                            status = 4
//                            isDialing = false
//                        }
//                    } else {
//                        status = 1
//                        isDialing = true
//                        lastDialNumber = incomingNumber
//                    }

                }
                else -> {
                    status = -1
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

