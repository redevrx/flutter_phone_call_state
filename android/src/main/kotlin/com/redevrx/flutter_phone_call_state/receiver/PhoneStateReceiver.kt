package com.redevrx.flutter_phone_call_state.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager


open class PhoneStateReceiver : BroadcastReceiver() {
    var status: Int = -1
    var phoneNumber: String? = null
    private var isIncoming = false

    private var lastState: Int = -1

    fun setup(context:Context){}

    override fun onReceive(context: Context?, intent: Intent?) {
        try {

//            val pState = intent?.getIntExtra("foreground_state", -2)
//            when (pState) {
//                0 -> println("IDLE")
//                3 -> println("DIALING")
//                4 -> println("ALERTING")
//                1 -> println("ACTIVE")
//            }
//            println("foreground_state: $pState")

            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            if(intent?.action.equals("android.intent.action.NEW_OUTGOING_CALL")){
                phoneNumber = intent?.getStringExtra("android.intent.extra.PHONE_NUMBER")
            }
            else {
                val incomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                val extraState = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

                extraState?.let {

                    var state = 0
                    when(it){
                        "IDLE" -> {
                            state = TelephonyManager.CALL_STATE_IDLE
                        }
                        "OFFHOOK" -> {
                            state = TelephonyManager.CALL_STATE_OFFHOOK
                        }
                        "RINGING" -> {
                            state = TelephonyManager.CALL_STATE_RINGING
                        }
                    }


                    onCallStateChanged(state, incomingNumber ?: "")
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *   case 0:
     *         return CallState.end;
     *       case 1:
     *         return CallState.outgoing;
     *       case 2:
     *         return CallState.incoming;
     *       case 3:
     *         return CallState.call;
     *       case 4:
     *         return CallState.outgoingAccept;
     *       case 5:
     *         return CallState.hold;
     *       case 6:
     *         return CallState.interruptAndHold;
     *       default:
     *         return CallState.none;
     */
    private fun onCallStateChanged(state: Int, number: String) {
        phoneNumber = number

        if (lastState == state) {
            return
        }

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                phoneNumber = number
                status = 2 /// incoming call
            }

            TelephonyManager.CALL_STATE_OFFHOOK ->                 //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    status = 1 ///outgoing
                } else {
                    isIncoming = true
                    status = 3 /// incoming answer
                }

            TelephonyManager.CALL_STATE_IDLE ->                 //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                status = if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    -1 //onMissedCall
                } else if (isIncoming) {
                    0
                    /// onIncomingCallEnded
                } else {
                    0
                    ///onOutgoingCallEnded
                }
        }
        lastState = state
    }
}

