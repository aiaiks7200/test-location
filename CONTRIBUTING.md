# Contributing to test Location

Thanks for your interest in test Location.

## Development Setup

Requirements:

- Android Studio or local Gradle installation
- Android SDK Platform 33
- JDK 8 or newer
- Android device or emulator running Android 5.0 or newer

Open the project in Android Studio and sync Gradle, or build with a local Gradle command:

```bash
gradle assembleDebug
```

This repository does not include production signing keys or private deployment credentials.

## Pull Requests

- Keep changes focused.
- Test mock-location behavior on a real Android device when possible.
- Update README or CHANGELOG when behavior changes.
- Do not commit APK files, signing keys, `.env` files, server credentials, payment secrets, or deployment records.

## Issues

When reporting a bug, include the Android version, device model, app version, reproduction steps, and relevant logcat output if available.

## License

Contributions are accepted only with permission from the project owner and are subject to the repository license.
