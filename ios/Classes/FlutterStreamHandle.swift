//
//  FlutterStreamHandle.swift
//
//  Created by redev.rx on 20/11/2024.
//

import Flutter
import UIKit
import CallKit

@objc class FlutterStreamHandle : NSObject, FlutterStreamHandler,CXCallObserverDelegate {
    private var eventSink: FlutterEventSink?
    private var callObserver: CXCallObserver!
    private var isOutgoing = false
    private var callback: (([String: Any]) -> Void)?
    private var backgroundTaskIdentifier: UIBackgroundTaskIdentifier = .invalid

    override init() {
       super.init()
       setupCallObserver()
    }

    @objc func beginBackgroundMonitoring() {
            print("Background monitoring started")

            callObserver = CXCallObserver()
            callObserver.setDelegate(self, queue: nil)

            beginBackgroundTask()
        }

   private func beginBackgroundTask() {
           self.backgroundTaskIdentifier = UIApplication.shared.beginBackgroundTask(expirationHandler: {
               self.endBackgroundTask()
           })

           if backgroundTaskIdentifier == .invalid {
               print("Failed to start background task")
           } else {
               print("Background task started")
           }
       }

       private func endBackgroundTask() {
           if backgroundTaskIdentifier != .invalid {
               UIApplication.shared.endBackgroundTask(backgroundTaskIdentifier)
               backgroundTaskIdentifier = .invalid
               print("Background task ended")
           }
       }

   @available(iOS 10.0, *)
   private func setupCallObserver() {
        callObserver = CXCallObserver()
        callObserver.setDelegate(self, queue: nil)
   }

   @available(iOS 10.0, *)
     public func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
     let phoneNumber = ""

          if call.hasEnded {
                    // Phone call ended
           send(data:["status": 0, "phoneNumber": phoneNumber])
          } else if call.isOutgoing && !call.hasConnected {
                     // Outgoing call, dialing
            send(data:["status": 1, "phoneNumber": phoneNumber]) // 1 for dialing
            isOutgoing = true
          } else if !call.isOutgoing && !call.hasConnected && !call.hasEnded {
                     // Incoming call, waiting to connect
             send(data:["status": 2, "phoneNumber": phoneNumber]) // 2 for incoming call
          } else if call.hasConnected && !call.hasEnded {
                     // Call is connected
              if(isOutgoing){
               send(data:["status": 4, "phoneNumber": phoneNumber]) // 4 for Outgoing connected call
               isOutgoing = false
              }else {
               send(data:["status": 3, "phoneNumber": phoneNumber]) // 3 for connected call
              }
          } else if call.isOnHold {
          ///click button Holding
             send(data:["status": 5, "phoneNumber": phoneNumber]) // Holding
          } else if call.isIncoming && call.hasConnected && !call.hasEnded {
          ///auto holing from callkit
            send(data:["status": 5, "phoneNumber": phoneNumber]) // Holding
          }else {
             send(data:["status": -1, "phoneNumber": phoneNumber])
          }
     }

    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self.eventSink = events
        return nil
    }


    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self.eventSink = nil
        return nil
    }

    public func send(data:[String: Any]){
    self.eventSink?(data)
    if let callback = callback {
                callback(data)
            }
    }

    public func setCallback(callback: @escaping ([String: Any]) -> Void) {
            self.callback = callback
    }
}