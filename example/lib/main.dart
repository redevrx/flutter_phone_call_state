import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter_phone_call_state/flutter_phone_call_state.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _phoneCallStatePlugin = PhoneCallState.instance;
  String status = 'none';

  @override
  void initState() {
    super.initState();
    subscriptionPhoneCallStateChange();
  }

  void subscriptionPhoneCallStateChange() async {
    _phoneCallStatePlugin.phoneStateChange.listen(
      (event) {
        debugPrint(
            "Phone Number: ${event.number}\nPhone Status:${event.state.name}");

        scheduleMicrotask(() {
          setState(() {
            status = event.state.name;
          });
        });
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Phone status: $status\n'),
        ),
      ),
    );
  }
}
