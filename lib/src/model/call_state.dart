/// Enum representing various call states for a phone call.
enum CallState {
  /// The call has ended.
  end,

  /// The call is being dialed (outgoing call).
  outgoing,

  /// The outgoing call has been accepted by the recipient.
  outgoingAccept,

  /// An incoming call is received.
  incoming,

  /// The incoming call has been accepted.
  call,

  /// holding.
  hold,

  /// interrupt call and holding current call.
  interruptAndHold,

  /// The call state is unknown or undefined.
  none,
}

extension CallStateExtension on CallState {
  /// Provides a description of the current call state.
  String get description {
    switch (this) {
      case CallState.end:
        return 'End call';
      case CallState.outgoing:
        return 'Dialing outgoing call';
      case CallState.outgoingAccept:
        return 'Outgoing call accepted';
      case CallState.incoming:
        return 'Incoming call';
      case CallState.call:
        return 'Call accepted';
      case CallState.hold:
        return "Holding";
      case CallState.interruptAndHold:
        return "InterruptAndHold";
      case CallState.none:
        return 'Unknown call state';
    }
  }
}
