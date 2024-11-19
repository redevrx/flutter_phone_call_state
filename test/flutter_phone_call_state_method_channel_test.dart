import 'package:flutter/services.dart';
import 'package:flutter_phone_call_state/src/model/call_state.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_phone_call_state/flutter_phone_call_state_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelFlutterPhoneCallState platform =
      MethodChannelFlutterPhoneCallState();
  const channel = EventChannel('flutter_phone_call_state');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockStreamHandler(channel, MockStreamHandler.inline(
      onListen: (arguments, events) {
        events.success({"status": 0, "phoneNumber": "0857200286"});
      },
    ));

    //     .setMockMethodCallHandler(
    //   channel,
    //   (MethodCall methodCall) async {
    //     return '42';
    //   },
    // );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockStreamHandler(channel, null);
  });

  test('phone state change', () async {
    final event = await platform.phoneStateChange().first;
    expect(event.state, CallState.end);
    expect(event.number, '0857200286');
  });
}
