class CallLogData {
  final String callType;
  final String number;
  final String date;
  final int duration;
  final bool isAnswer;

  CallLogData({
    required this.callType,
    required this.number,
    required this.date,
    required this.duration,
    required this.isAnswer,
  });

  factory CallLogData.fromJson(Map<String, dynamic> json) {
    return CallLogData(
      callType: json['callType'] ?? '',
      number: json['number'] ?? '',
      date: '${json['date'] ?? ''}',
      duration: json['duration'] ?? 0,
      isAnswer: json['isAnswer'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'callType': callType,
      'number': number,
      'date': date,
      'duration': duration,
      'isAnswer': isAnswer,
    };
  }
}
