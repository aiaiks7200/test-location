# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.13] - 2026-06-12

### Added
- Recent locations quick-start feature (save up to 3 recent mock locations)
- Multi-language support: English, 中文, 日本語, 한국어, Русский, Français, Deutsch
- Server-side license verification with 48-hour offline cache
- Forced upgrade mechanism from server
- Device-code binding for subscription management
- Anti-rollback protection for subscription state
- Signature verification to prevent APK tampering
- Anti-debug and anti-Xposed detection
- Root detection (advisory, non-blocking)
- Payment integration with Alipay and WeChat Pay
- Dynamic plan pricing from server API

### Changed
- Upgraded to Leaflet.js for map rendering
- Improved mock location stability with dual-provider support (GPS + Network)
- Enhanced foreground service notification

### Fixed
- Mock location provider crash on some Android 13 devices
- Memory leak in route simulation handler

## [1.08] - 2026-04-01

### Added
- Initial release
- Fixed-point mock location
- Route simulation between two points
- Adjustable movement speed
- Map-based location selection (OpenStreetMap + Leaflet)
- 15-day free trial
- Subscription payment support
- User agreement and privacy policy
- Developer Options mock location app guide

### Changed
- Minimum SDK: Android 5.0 (API 21)
- Target SDK: Android 13 (API 33)
