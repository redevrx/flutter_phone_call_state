import 'package:flutter/services.dart';
import 'package:flutter_phone_call_state/src/model/call_result.dart';

import 'flutter_phone_call_state_platform_interface.dart';

/// An implementation of [FlutterPhoneCallStatePlatform] that uses method channels.
class MethodChannelFlutterPhoneCallState extends FlutterPhoneCallStatePlatform {
  /// The method channel used to interact with the native platform.
  final _eventChannel = const EventChannel('flutter_phone_call_state');

  /// phoneStateChange
  /// ios callkit
  /// android telephonyManager
  /// [phoneStateChange]
  @override
  Stream<CallResult> phoneStateChange() {
    return _eventChannel
        .receiveBroadcastStream()
        .map((event) => CallResult.fromFlag(event))
        .asBroadcastStream()
        .distinct();
  }
}
