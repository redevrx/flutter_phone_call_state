import 'dart:ui';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_phone_call_state_method_channel.dart';

abstract class FlutterPhoneCallStatePlatform extends PlatformInterface {
  /// Constructs a FlutterPhoneCallStatePlatform.
  FlutterPhoneCallStatePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterPhoneCallStatePlatform _instance =
      MethodChannelFlutterPhoneCallState();

  /// The default instance of [FlutterPhoneCallStatePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterPhoneCallState].
  static FlutterPhoneCallStatePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterPhoneCallStatePlatform] when
  /// they register themselves.
  static set instance(FlutterPhoneCallStatePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  void initCallState() {
    throw UnimplementedError('init() has not been implemented.');
  }

  void setIncomingHandler(VoidCallback callback) {
    throw UnimplementedError('has not been implemented.');
  }

  void setDialingHandler(VoidCallback callback) {
    throw UnimplementedError('has not been implemented.');
  }

  void setConnectedHandler(VoidCallback callback) {
    throw UnimplementedError('has not been implemented.');
  }

  void setDisconnectedHandler(VoidCallback callback) {
    throw UnimplementedError('has not been implemented.');
  }

  void setErrorHandler(ErrorHandler handler) {
    throw UnimplementedError('has not been implemented.');
  }
}
