## Table of Contents
- [Description](#description)
- [Features](#features)
- [Native Integration](#native-integration)
- [Pay Attention](#pay-attention)
- [How to Install](#how-to-install)
    - [Flutter](#flutter)
    - [Android Permissions](#android-permissions)
    - [Ios Permissions](#ios-permissions)
    - [Ios Background Handle](#ios-background-monitor-handler)
- [How to Use](#how-to-use)
    - [Get Stream Phone Call State](#get-stream-phone-call-state)
    - [Available Call States](#available-call-states)
    - [Available Android Only](#available-android-only)
- [Example App](#example-app)


<br>
<p align="center">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/redevrx/flutter_phone_call_state">
<img alt="GitHub contributors" src="https://img.shields.io/github/contributors/redevrx/flutter_phone_call_state">
<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/redevrx/flutter_phone_call_state?style=social">
<img alt="GitHub Workflow Status" src="https://img.shields.io/github/actions/workflow/status/redevrx/flutter_phone_call_state/dart.yml?label=tests">
<img alt="GitHub" src="https://img.shields.io/github/license/redevrx/flutter_phone_call_state">
<img alt="Pub Points" src="https://img.shields.io/pub/points/flutter_phone_call_state">
<img alt="Pub Popularity" src="https://img.shields.io/pub/popularity/flutter_phone_call_state">
<img alt="Pub Likes" src="https://img.shields.io/pub/likes/flutter_phone_call_state">
<img alt="Pub Version" src="https://img.shields.io/pub/v/flutter_phone_call_state">
<img alt="Code Coverage" src="https://img.shields.io/codecov/c/github/redevrx/flutter_phone_call_state?logo=codecov&color">
</p>
</br>


## DESCRIPTION
This Flutter plugin allows you to track and manage phone call states on Android and iOS devices. It helps you monitor the current state of a call (incoming, outgoing, accepted, or ended) and provides real-time updates.

## Features
- Detects the current phone call status (incoming, answered, or ended).
- Supports both Android and iOS platforms.
- Provides real-time call state updates.
- Simple and easy-to-use API.


## Native Integration
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
  flutter_phone_call_state: 0.1.0
```

## Android Permissions
#### Android: Added permission on manifest
```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_PHONE_CALL"/>
<uses-permission android:name="android.permission.MANAGE_OWN_CALLS"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

<service
android:name="com.redevrx.flutter_phone_call_state.receiver.CallMonitoringService"
android:exported="false"
android:foregroundServiceType="phoneCall"/>
```
> **Warning**: Adding `READ_CALL_LOG` permission, your app will be removed from the Play Store if you don't have a valid reason to use it. [Read more](https://support.google.com/googleplay/android-developer/answer/9047303?hl=en). But if you don't add it, you will not be able to know caller's number.

## Ios Permissions
#### Ios: Added permission on list.info
```xml
<key>UIBackgroundModes</key>
<array>
<!--add more permission-->
</array>
```

## Ios Background Monitor Handler

```
import flutter_phone_call_state
import ...

@main
@objc class AppDelegate: FlutterAppDelegate {
    private var backgroundTaskIdentifier: UIBackgroundTaskIdentifier = .invalid
    
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  )....
    
    func initBackground(){
        var backgroundTaskIdentifier:UIBackgroundTaskIdentifier = .invalid
        
        backgroundTaskIdentifier = UIApplication.shared.beginBackgroundTask(expirationHandler: {
                UIApplication.shared.endBackgroundTask(backgroundTaskIdentifier)
        })
    }
    
    override func applicationWillEnterForeground(_ application: UIApplication) {
        super.applicationWillEnterForeground(application)
        FlutterPhoneCallStatePlugin.shared.initState()
    }
    
    override func applicationDidEnterBackground(_ application: UIApplication) {
        super.applicationDidEnterBackground(application)
        
        ///using beginBackgroundMonitoring
        FlutterPhoneCallStatePlugin.shared.beginBackgroundMonitoring()
        
        //or using custom
        initBackground()
    }
}
```

> **Warning** The background task should be called before the app enters background mode or app mini mode, or at the moment when the call is initiated. If called directly in applicationDidEnterBackground, the plugin might not work properly.

## Android Background Monitor Handler
```dart
  Future<void> requestPermission() async {
  final results = await [Permission.notification, Permission.phone].request();

  if (results[Permission.notification]?.isGranted == true &&
          results[Permission.phone]?.isGranted == true) {
    if (Platform.isAndroid) {
      PhoneCallState.instance.startMonitorService();
    }
  }
}
```

## HOW TO USE

### Get stream Phone Call State

```dart
final _phoneCallStatePlugin = PhoneCallState.instance;
void subscriptionPhoneCallStateChange() async {
  _phoneCallStatePlugin.phoneStateChange.listen(
        (event) {
          switch (event.state) {
            case CallState.end:
            // TODO: Handle this case.
            case CallState.outgoing:
            // TODO: Handle this case.
            case CallState.outgoingAccept:
            // TODO: Handle this case.
            case CallState.incoming:
            // TODO: Handle this case.
            case CallState.call:
            // TODO: Handle this case.
            case CallState.none:
            // TODO: Handle this case.
            case CallState.hold:
            // TODO: Handle this case.
            case CallState.interruptAndHold:
            // TODO: Handle this case.
          }

      debugPrint(event.state.description);
    },
  );
}
```

### Available Call States:
The `CallState` enum represents various states during a phone call. Each state describes a specific point during the lifecycle of a call. Below are the possible call states:

- **`CallState.end`**:  
  The call has ended. This occurs when the call is either disconnected by the caller or the recipient.

- **`CallState.outgoing`**:  
  The call is in the process of being dialed (outgoing call). This state is active right after the user initiates the call but before the recipient has answered.

- **`CallState.outgoingAccept`**:  
  The outgoing call has been accepted by the recipient. This state indicates that the recipient has answered the call and the conversation is now live.

- **`CallState.incoming`**:  
  An incoming call is being received. The device is receiving the call, but the user has not answered it yet. This state is active when the phone is ringing.

- **`CallState.call`**:  
  The incoming call has been accepted. This state is triggered once the incoming call is answered by the user, and the conversation is ongoing.

- **`CallState.hold`**:  
  User press button Hold.

- **`CallState.interruptAndHold`**:  
  Interrupt the call and hold the current call to answer the new one.

- **`CallState.none`**:  
  The call state is unknown or undefined. This state is used when the call state cannot be determined or is not applicable.

These states help you track the progress of a phone call, allowing you to respond accordingly within your Flutter application.

### Available Android Only:

- **`onStateChange`**:
  Return call phone state in callback function.

- **`getLastCallLog`**:
  Return call log from last call or after last call.

```dart

class _MyAppState extends State<MyApp> {
  final _phoneCallStatePlugin = PhoneCallState.instance;
  String status = 'none';

  @override
  void initState() {
    super.initState();
    requestPermission();
  }

  Future<void> requestPermission() async {
    final results = await [Permission.notification, Permission.phone].request();

    if (results[Permission.notification]?.isGranted == true &&
            results[Permission.phone]?.isGranted == true) {
      if (Platform.isAndroid) {
        PhoneCallState.instance.startMonitorService();

         ///wait start service.
        await Future.delayed(const Duration(seconds: 2));
        subscriptionPhoneCallStateChange();
      }
    }

    if (Platform.iOS) {
      subscriptionPhoneCallStateChange();
     }
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

## Example App
<img src="https://github.com/redevrx/flutter_phone_call_state/blob/main/assets/example.gif?raw=true" width="350"  alt="Example Video App"/>
