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
}
