import 'package:flutter_phone_call_state/src/model/call_state.dart';

/// result call status
/// and phone number
/// [CallResult]
class CallResult {
  ///[state]
  final CallState state;

  ///[number]
  final String number;

  CallResult({
    required this.state,
    this.number = '',
  });

  static CallResult fromFlag(Map data) {
    final flag = data['status'] as int? ?? -1;
    return CallResult(
      state: from(flag),
      number: data['phoneNumber'] ?? '',
    );
  }

  /// Creates a [CallState] from an integer value.
  static CallState from(int value) {
    switch (value) {
      case 0:
        return CallState.end;
      case 1:
        return CallState.outgoing;
      case 2:
        return CallState.incoming;
      case 3:
        return CallState.call;
      case 4:
        return CallState.outgoingAccept;
      default:
        return CallState.none;
    }
  }
}
