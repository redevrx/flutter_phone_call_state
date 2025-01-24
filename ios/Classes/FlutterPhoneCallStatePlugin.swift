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

   public func beginBackgroundMonitoring()  {
      self.handler?.beginBackgroundMonitoring()
   }

   public func endBackgroundTask()  {
         self.handler?.endBackgroundTask()
      }

}