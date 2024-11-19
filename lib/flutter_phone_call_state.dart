import 'package:flutter_phone_call_state/src/model/call_result.dart';

import 'flutter_phone_call_state_platform_interface.dart';

class FlutterPhoneCallState {
  final _instance = FlutterPhoneCallStatePlatform.instance;

  Stream<CallResult> get phoneStateChange => _instance.phoneStateChange();
}
