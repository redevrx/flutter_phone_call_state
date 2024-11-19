import 'dart:ui';

import 'flutter_phone_call_state_method_channel.dart';
import 'flutter_phone_call_state_platform_interface.dart';

class FlutterPhoneCallState {
  final _instance = FlutterPhoneCallStatePlatform.instance;
  Future<String?> getPlatformVersion() {
    return _instance.getPlatformVersion();
  }

  void init() => _instance.initCallState();

  void setIncomingHandler(VoidCallback callback) {
   _instance.setIncomingHandler(callback);
  }

  void setDialingHandler(VoidCallback callback) {
    _instance.setDialingHandler(callback);
  }

  void setConnectedHandler(VoidCallback callback) {
    _instance.setConnectedHandler(callback);
  }

  void setDisconnectedHandler(VoidCallback callback) {
    _instance.setDisconnectedHandler(callback);
  }

  void setErrorHandler(ErrorHandler handler) {
    _instance.setErrorHandler(handler);
  }
}
