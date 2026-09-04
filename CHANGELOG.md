## 0.2.0
- Migrate to Flutter built-in Kotlin: stop applying `kotlin-android` on AGP 9+
  (removes the "plugins that apply Kotlin Gradle Plugin" warning). KGP is still
  applied on AGP 8 so older apps keep building.
- Android toolchain: AGP 9.1.0, Kotlin 2.4.0, compileSdk 36, Java/jvmTarget 17
- `android.kotlinOptions` -> top-level `kotlin { compilerOptions { } }`
- **Breaking**: minSdk 21 -> 24 (Flutter >= 3.44 requires API 24)
- Requires Flutter >= 3.44.0 / Dart >= 3.12.0
- No behaviour change: call-state and call-log logic is untouched

## 0.1.0
- Android Add startMonitorService

## 0.0.9
- Fix Bug Start Service in Android 14

## 0.0.8
- Fix Bug Start Service in Android 14

## 0.0.7
- Fix Bug ios
- Add Android Get Call log (only android)
- Add Android onStateChange (only android)

## 0.0.6
 - Fix Bug ios

## 0.0.5
- Refactor Code
- Add Hold
- Add Interrupt Call

## 0.0.4
- Refactor Code
- Add status outgoing Accept

## 0.0.3
- Add example video

## 0.0.2
- Refactor android phone call status

## 0.0.1
 - public package
