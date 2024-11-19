import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_phone_call_state_platform_interface.dart';

typedef ErrorHandler = void Function(String message);

/// An implementation of [FlutterPhoneCallStatePlatform] that uses method channels.
class MethodChannelFlutterPhoneCallState extends FlutterPhoneCallStatePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_phone_call_state');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  void initCallState() {
    methodChannel.setMethodCallHandler(platformCallHandler);
  }


  VoidCallback? incomingHandler;
  VoidCallback? dialingHandler;
  VoidCallback? connectedHandler;
  VoidCallback? disconnectedHandler;
  ErrorHandler? errorHandler;

  @override
  void setIncomingHandler(VoidCallback callback) {
    incomingHandler = callback;
  }

  @override
  void setDialingHandler(VoidCallback callback) {
    dialingHandler = callback;
  }

  @override
  void setConnectedHandler(VoidCallback callback) {
    connectedHandler = callback;
  }

  @override
  void setDisconnectedHandler(VoidCallback callback) {
    disconnectedHandler = callback;
  }

  @override
  void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }

  Future<void> platformCallHandler(MethodCall call) async {
    debugPrint("_platformCallHandler call ${call.method} ${call.arguments}");
    switch (call.method) {
      case "phone.incoming":
        if (incomingHandler != null) {
          incomingHandler!();
        }
        break;
      case "phone.dialing":
        if (dialingHandler != null) {
          dialingHandler!();
        }
        break;
      case "phone.connected":
        if (connectedHandler != null) {
          connectedHandler!();
        }
        break;
      case "phone.disconnected":
        if (disconnectedHandler != null) {
          disconnectedHandler!();
        }
        break;
      case "phone.onError":
        if (errorHandler != null) {
          errorHandler!(call.arguments);
        }
        break;
      default:
        debugPrint('Unknown method ${call.method} ');
    }
  }
}
