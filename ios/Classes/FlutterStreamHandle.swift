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
    private var isTaskRunning = false

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
        guard !isTaskRunning else {
               print("Background task is already running. Skipping new task start.")
               return
           }

        self.backgroundTaskIdentifier = UIApplication.shared.beginBackgroundTask(expirationHandler: {
               print("Background task is about to expire!")
               self.endBackgroundTask()
        })

        if  self.backgroundTaskIdentifier ==  .invalid {
               print("Failed to start background task")
               return
        } else {
               print("Background task started")
               DispatchQueue.global().async {

                sleep(60)
                print("1 minute passed")

                print("Background work completed")
                self.endBackgroundTask()
               }
           }
       }

       private func endBackgroundTask() {
          guard isTaskRunning else {
                   print("Background task is already ended. Skipping end.")
                   return
               }

           if self.backgroundTaskIdentifier != .invalid {
               UIApplication.shared.endBackgroundTask(self.backgroundTaskIdentifier)
               self.backgroundTaskIdentifier = .invalid
               isTaskRunning = false
               print("Background task ended")

               DispatchQueue.global().async {
                           sleep(2)
                           print("request start background task")
                           self.beginBackgroundTask()
                       }
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
              if let call = callObserver.calls.first(where: { $0 != call }) {
                          // callkit auto holing return status accept interrupt call
                          send(data: ["status": 6, "phoneNumber": phoneNumber])
                          return
              }

              if(isOutgoing){
               send(data:["status": 4, "phoneNumber": phoneNumber]) // 4 for Outgoing connected call
               isOutgoing = false
              }else {
               send(data:["status": 3, "phoneNumber": phoneNumber]) // 3 for connected call
              }
          } else if call.isOnHold {
          ///click button Holding
             send(data:["status": 5, "phoneNumber": phoneNumber]) // Holding
          } else {
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