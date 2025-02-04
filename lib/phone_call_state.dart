import 'package:flutter_phone_call_state/src/model/call_log.dart';
import 'package:flutter_phone_call_state/src/model/call_result.dart';

import 'flutter_phone_call_state_platform_interface.dart';

class PhoneCallState {
  PhoneCallState._internal();
  static final _instance = PhoneCallState._internal();
  static PhoneCallState get instance => _instance;

  /// platform api
  final _phoneCallState = FlutterPhoneCallStatePlatform.instance;

  /// get phone call state change
  /// [phoneStateChange]
  Stream<CallResult> get phoneStateChange => _phoneCallState.phoneStateChange();

  void onStateChange({required void Function(CallResult result) callback}) =>
      _phoneCallState.onStateChange(callback: callback);

  Future<CallLog> getLastCallLog() => _phoneCallState.getLastCall();
}
