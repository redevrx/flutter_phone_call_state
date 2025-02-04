import 'package:flutter/services.dart';
import 'package:flutter_phone_call_state/src/model/call_log.dart';
import 'package:flutter_phone_call_state/src/model/call_result.dart';

import 'flutter_phone_call_state_platform_interface.dart';

/// An implementation of [FlutterPhoneCallStatePlatform] that uses method channels.
class MethodChannelFlutterPhoneCallState extends FlutterPhoneCallStatePlatform {
  /// The method channel used to interact with the native platform.
  final _eventChannel = const EventChannel('flutter_phone_call_state');
  final _methodChannel =
      const MethodChannel('flutter_phone_call_state_channel');

  @override
  void onStateChange({required void Function(CallResult result) callback}) {
    _methodChannel.setMethodCallHandler((call) async {
      if (call.method == "state_change") {
        final result = CallResult.fromFlag(call.arguments);

        callback(result);
      }
    });
  }

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

  @override
  Future<CallLog> getLastCall() async {
    final arg = await _methodChannel.invokeMethod("check_last_call");
    return CallLog.fromJson(arg);
  }
}
