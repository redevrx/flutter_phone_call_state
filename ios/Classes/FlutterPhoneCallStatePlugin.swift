import Flutter
import UIKit

public class FlutterPhoneCallStatePlugin: NSObject, FlutterPlugin {
//    var handler:FlutterStreamHandle?
//
//    public static func register(with registrar: FlutterPluginRegistrar) {
//         let instance = FlutterPhoneCallStatePlugin()
//          self.handler = FlutterStreamHandle()
//
//         let eventChannel = FlutterEventChannel(name: "flutter_phone_call_state", binaryMessenger: registrar.messenger())
//         eventChannel.setStreamHandler(self.handler!)
//
//         ///handler.beginBackgroundMonitoring()
//      }
//
//      public func initState(){
//      self.handler?.reSetup()
//      }

   var handler: FlutterStreamHandle?

       // Shared instance
   public static let shared = FlutterPhoneCallStatePlugin()

   public static func register(with registrar: FlutterPluginRegistrar) {
           let instance = FlutterPhoneCallStatePlugin.shared // ใช้ shared instance
           instance.handler = FlutterStreamHandle()

           let eventChannel = FlutterEventChannel(name: "flutter_phone_call_state", binaryMessenger: registrar.messenger())
           eventChannel.setStreamHandler(instance.handler!)
   }

   public func initState() {
           self.handler?.reSetup()
   }

}

// public class FlutterPhoneCallStatePlugin: NSObject, FlutterPlugin, CXCallObserverDelegate, FlutterStreamHandler {
//     private var callObserver: CXCallObserver!
//     private var eventSink: FlutterEventSink?
//     private var handler:FlutterStreamHandler
//
//     // Registering the EventChannel and MethodChannel
//     public static func register(with registrar: FlutterPluginRegistrar) {
//         let instance = FlutterPhoneCallStatePlugin()
//
//
//         // Register the EventChannel and set the StreamHandler
//         let eventChannel = FlutterEventChannel(name: "flutter_phone_call_state", binaryMessenger: registrar.messenger())
//         eventChannel.setStreamHandler(self.handler)
//     }
//
//     // Initializer to set up call observer
//     override init() {
//         super.init()
//         setupCallObserver()
//     }
//
//     // Stream handler: When a listener subscribes to the event stream
//     public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
//         self.eventSink = events
//         return nil
//     }
//
//     // Stream handler: When a listener cancels the subscription
//     public func onCancel(withArguments arguments: Any?) -> FlutterError? {
//         self.eventSink = nil
//         return nil
//     }
//
//     // Set up the call observer to listen for call state changes
//     @available(iOS 10.0, *)
//     func setupCallObserver() {
//         callObserver = CXCallObserver()
//         callObserver.setDelegate(self, queue: nil)
//     }
//
//     // Handle call state changes and send updates via the EventChannel
//     @available(iOS 10.0, *)
//     public func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
//         if call.hasEnded {
//             // Phone call ended
//             self.eventSink?(["status": 0, "phoneNumber": ""]) // 0 for call ended
//         } else if call.isOutgoing && !call.hasConnected {
//             // Outgoing call, dialing
//             self.eventSink?(["status": 1, "phoneNumber": ""]) // 1 for dialing
//         } else if !call.isOutgoing && !call.hasConnected && !call.hasEnded {
//             // Incoming call, waiting to connect
//             self.eventSink?(["status": 2, "phoneNumber": ""]) // 2 for incoming call
//         } else if call.hasConnected && !call.hasEnded {
//             // Call is connected
//             self.eventSink?(["status": 3, "phoneNumber": ""]) // 3 for connected call
//         } else {
//          self.eventSink?(["status": -1, "phoneNumber": ""])
//         }
//     }
// }