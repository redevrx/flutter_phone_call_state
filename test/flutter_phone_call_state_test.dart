import 'dart:ui';

import 'package:flutter_phone_call_state/src/model/call_result.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_phone_call_state/flutter_phone_call_state.dart';
import 'package:flutter_phone_call_state/flutter_phone_call_state_platform_interface.dart';
import 'package:flutter_phone_call_state/flutter_phone_call_state_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterPhoneCallStatePlatform
    with MockPlatformInterfaceMixin
    implements FlutterPhoneCallStatePlatform {
  @override
  Stream<CallResult> phoneStateChange() {
    // TODO: implement phoneStateChange
    throw UnimplementedError();
  }

}

void main() {
  final FlutterPhoneCallStatePlatform initialPlatform = FlutterPhoneCallStatePlatform.instance;

  test('$MethodChannelFlutterPhoneCallState is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterPhoneCallState>());
  });

  test('getPlatformVersion', () async {
    FlutterPhoneCallState flutterPhoneCallStatePlugin = FlutterPhoneCallState();
    MockFlutterPhoneCallStatePlatform fakePlatform = MockFlutterPhoneCallStatePlatform();
    FlutterPhoneCallStatePlatform.instance = fakePlatform;

   // expect(await flutterPhoneCallStatePlugin.getPlatformVersion(), '42');
  });
}
