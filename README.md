# test Location

test Location / TEST LOCATION is an Android mock-location app for development, debugging, and location workflow testing. It lets users select coordinates from a map, start or stop mock GPS output, save favorite points, reuse recent mock locations, and manage app authorization from the official backend.

Official website: <https://dw.locati.xyz>

## Features

- Mock GPS location by entering latitude and longitude or selecting a point on the built-in map.
- Route mock mode and foreground location service for continuous simulation.
- Favorites and recent locations for faster repeated testing.
- Multi-language Android resources.
- Trial, subscription, license check, version check, and payment flow integration.
- In-app privacy policy, user agreement, settings, and update guidance.

## Current Release

- App name: `test Location`
- Version: `1.13`
- Android package: `com.test.mocklocation`
- Minimum Android version: Android 5.0, API 21
- Target SDK: Android 13, API 33

Download the APK from the GitHub Releases page for this repository.

## How To Use

1. Install the APK on an Android device.
2. Open Android Settings and enable Developer Options.
3. In Developer Options, choose `Select mock location app`.
4. Select `test Location`.
5. Open the app, grant location permission, choose a coordinate on the map or enter latitude and longitude manually, then tap Start Mock.
6. Tap Stop Mock when testing is complete.

## Build From Source

Requirements:

- Android Studio or local Gradle installation
- Android SDK Platform 33
- Android Build Tools compatible with AGP 7.4.2
- JDK 8 or newer

Build steps:

```bash
gradle assembleDebug
```

The original local package did not include a complete Gradle Wrapper jar, so this public repository expects Android Studio or a local Gradle command.

## Project Structure

```text
app/
  src/main/
    AndroidManifest.xml
    java/com/test/mocklocation/
    res/
    assets/
build.gradle
settings.gradle
gradle.properties
```

## Permissions

- `INTERNET`: backend API and payment flow.
- `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`: location and mock-location workflows.
- `ACCESS_MOCK_LOCATION`: mock location support on compatible Android versions.
- `FOREGROUND_SERVICE` / `FOREGROUND_SERVICE_LOCATION`: continuous mock-location service.
- `POST_NOTIFICATIONS`: foreground service notification on newer Android versions.

## Security Notes

Signing keys, server credentials, admin accounts, deployment records, and payment secrets are intentionally not included in this repository. Keep production credentials on the server side or in private build infrastructure.

## Legal And Responsible Use

This app is intended for development, debugging, QA, and compatibility testing. Users are responsible for complying with applicable laws, platform rules, and service terms when using mock-location features.

## License

Copyright (c) 2026 TEST LOCATION. All rights reserved.
