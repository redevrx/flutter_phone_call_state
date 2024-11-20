import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter_phone_call_state/flutter_phone_call_state.dart';
import 'package:url_launcher/url_launcher_string.dart';

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
            "Phone Number: ${event.number} <------> Phone Status:${event.state.name}");

        scheduleMicrotask(() {
          setState(() {
            status = '${event.state.name}: ${event.number}';
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
        body: Column(
          children: [
            InkWell(
              child: const Text("tell:0824112873"),
              onTap: () async {
                ///call here
                await launchUrlString("tel://0824112873");
              },
            ),
            Center(
              child: Text('Phone status: $status\n'),
            ),
          ],
        ),
      ),
    );
  }
}
