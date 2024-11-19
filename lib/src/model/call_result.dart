import 'package:flutter_phone_call_state/src/model/call_state.dart';

class CallResult {
  final CallState state;
  final String number;

  CallResult({
    required this.state,
    this.number = '',
  });

  static CallResult fromFlag(Map data) {
    final flag = data['status'] as int? ?? -1;

    late CallState state;
    switch (flag) {
      case 0:
        state = CallState.end;
        break;
      case 1:
        state = CallState.outgoing;
        break;
      case 2:
        state = CallState.incoming;
        break;
      case 3:
        state = CallState.call;
        break;
      default:
        state = CallState.none;
        break;
    }

    return CallResult(
      state: state,
      number: data['phoneNumber'] ?? '',
    );
  }
}
