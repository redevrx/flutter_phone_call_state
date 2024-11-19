import Flutter
import UIKit
import CallKit

public class FlutterPhoneCallStatePlugin: NSObject, FlutterPlugin, CXCallObserverDelegate {
    var callObserver: CXCallObserver!
    var _channel: FlutterMethodChannel

    var testTimer: Timer?
    var counter = 0

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_phone_call_state", binaryMessenger: registrar.messenger())
        let instance = FlutterPhoneCallStatePlugin(channel: channel) // Pass the channel here
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    init(channel: FlutterMethodChannel) {
        self._channel = channel
        super.init()
        setupCallObserver()
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            result("iOS " + UIDevice.current.systemVersion)
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    @available(iOS 10.0, *)
    func setupCallObserver() {
        callObserver = CXCallObserver()
        callObserver.setDelegate(self, queue: nil)
    }

    @available(iOS 10.0, *)
    public func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
        if call.hasEnded {
            print("CXCallState :Disconnected")
            self._channel.invokeMethod("phone.disconnected", arguments: nil)
        }
        if call.isOutgoing && !call.hasConnected {
            print("CXCallState :Dialing")
            self._channel.invokeMethod("phone.dialing", arguments: nil)
        }
        if !call.isOutgoing && !call.hasConnected && !call.hasEnded {
            print("CXCallState :Incoming")
            self._channel.invokeMethod("phone.incoming", arguments: nil)
        }
        if call.hasConnected && !call.hasEnded {
            print("CXCallState :Connected")
            self._channel.invokeMethod("phone.connected", arguments: nil)
        }
    }
}

//
//public class FlutterPhoneCallStatePlugin: NSObject, FlutterPlugin,CXCallObserverDelegate {
//  var callObserver: CXCallObserver!
//    var _channel: FlutterMethodChannel
//
//    var testTimer: Timer?
//    var counter = 0
//
//  public static func register(with registrar: FlutterPluginRegistrar) {
//    let channel = FlutterMethodChannel(name: "flutter_phone_call_state", binaryMessenger: registrar.messenger())
//    let instance = FlutterPhoneCallStatePlugin()
//    registrar.addMethodCallDelegate(instance, channel: channel)
//
//  }
//
//  init(channel:FlutterMethodChannel){
//    super.init()
//     _channel = channel
//     setupCallObserver()
// }
//
//  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
//    switch call.method {
//    case "getPlatformVersion":
//      result("iOS " + UIDevice.current.systemVersion)
//    default:
//      result(FlutterMethodNotImplemented)
//    }
//  }
//
//  @available(iOS 10.0,*)
//  func setupCallObserver(){
//              callObserver = CXCallObserver()
//              callObserver.setDelegate(self, queue: nil)
// }
//
//   @available(iOS 10.0,*)
//   public func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
//        if call.hasEnded == true {
//            print("CXCallState :Disconnected")
//             _channel.invokeMethod("phone.disconnected", arguments: nil)
//        }
//        if call.isOutgoing == true && call.hasConnected == false {
//            print("CXCallState :Dialing")
//             _channel.invokeMethod("phone.dialing", arguments: nil)
//        }
//        if call.isOutgoing == false && call.hasConnected == false && call.hasEnded == false {
//            print("CXCallState :Incoming")
//             _channel.invokeMethod("phone.incoming", arguments: nil)
//        }
//
//        if call.hasConnected == true && call.hasEnded == false {
//            print("CXCallState : Connected")
//              _channel.invokeMethod("phone.connected", arguments: nil)
//        }
//    }
//}
