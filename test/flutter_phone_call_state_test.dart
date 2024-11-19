import 'package:flutter/cupertino.dart';
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
    return Stream.value(
        CallResult(state: CallState.call, number: "0857200286"));
  }
}

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  final FlutterPhoneCallStatePlatform initialPlatform =
      FlutterPhoneCallStatePlatform.instance;

  test('$MethodChannelFlutterPhoneCallState is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterPhoneCallState>());
  });

  test('test phone state change', () async {
    final flutterPhoneCallStatePlugin = PhoneCallState.instance;
    MockFlutterPhoneCallStatePlatform fakePlatform =
        MockFlutterPhoneCallStatePlatform();
    FlutterPhoneCallStatePlatform.instance = fakePlatform;
    final result = CallResult(state: CallState.call, number: "0857200286");

    flutterPhoneCallStatePlugin.phoneStateChange.listen(
      (event) {
        expect(event.number, result.number);
        expect(event.state, result.state);
      },
    );
  });
}
