## DESCRIPTION

This plugin allows you to know quickly and easily if your Android or iOS device is receiving a call and to know the status of the call.

- Native Android: [TelephonyManager](https://developer.android.com/reference/android/telephony/TelephonyManager)
- Native iOS: [CallKit](https://developer.apple.com/documentation/callkit)

## PAY ATTENTION

- In the iOS simulator doesn't work
- The phone number is only obtainable on Android!

## HOW TO INSTALL
#### Flutter
```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_phone_call_state: 0.0.1
```
#### Android: Added permission on manifest
```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
```
> **Warning**: Adding `READ_CALL_LOG` permission, your app will be removed from the Play Store if you don't have a valid reason to use it. [Read more](https://support.google.com/googleplay/android-developer/answer/9047303?hl=en). But if you don't add it, you will not be able to know caller's number.

## HOW TO USE

### Get stream Phone Call State

```dart
StreamBuilder(
Stream:PhoneCallState.instance.phoneStateChange
....
```


```dart

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

        scheduleMicrotask((){
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
```