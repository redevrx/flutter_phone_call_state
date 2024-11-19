package com.example.flutter_phone_call_state.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.PhoneStateListener


open class PhoneStateReceiver : BroadcastReceiver() {
    var status: Int = -1
    var phoneNumber: String? = null

    fun instance(context: Context) {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
        phoneNumber = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            // ดึงเบอร์โทรที่กำลังโทรเข้ามา
            val incomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // ตรวจสอบสถานะการโทรจาก EXTRA_STATE
            status = when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> 2  // โทรเข้า
                TelephonyManager.EXTRA_STATE_OFFHOOK -> 3
                TelephonyManager.EXTRA_STATE_IDLE -> 0  // โทรเสร็จสิ้น
                else -> -1  // ไม่มีการโทร
            }

            phoneNumber = incomingNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val mPhoneListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            try {
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> {
                        status = 0  // โทรเสร็จสิ้น
                        phoneNumber = null
                    }
                    TelephonyManager.CALL_STATE_RINGING -> {
                        status = 2  // โทรเข้า
                        phoneNumber = incomingNumber
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        status = 3
                    }
                    else -> {
                        status = -1  // สถานะอื่นๆ
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

