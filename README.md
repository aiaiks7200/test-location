# test Location

**中文名**：位置模拟软件 / 模拟定位软件  
**英文名**：test Location / TEST LOCATION

test Location 是一款 Android 位置模拟软件，面向开发调试、测试验证、定位相关功能排查等场景。软件支持模拟定位、地图选点、经纬度输入、模拟路线、收藏地点、最近模拟地点、会员授权和版本更新检查。

test Location / TEST LOCATION is an Android mock location and route simulation app for development, debugging, QA, and location workflow testing. It lets users select coordinates from a map, start or stop mock GPS output, simulate routes, save favorite points, reuse recent mock locations, and manage app authorization from the official backend.

Official website: <https://dw.locati.xyz>

## 中文介绍

test Location 是一款用于 Android 设备的位置模拟软件，也可称为模拟定位软件、虚拟位置工具或 Mock Location 工具。用户可以通过地图选择位置，也可以手动输入经纬度，一键开启模拟定位；同时支持模拟路线功能，可用于测试位置移动、路线轨迹、地图应用、定位服务、签到类流程、附近位置展示等开发和测试场景。

主要能力：

- 模拟定位：输入经纬度或在地图上选点后，启动虚拟 GPS 位置。
- 模拟路线：按路线方式模拟位置移动，适合测试移动轨迹和定位变化。
- 地图选点：内置地图页面，支持搜索、点击选点和坐标回传。
- 最近位置：保留最近使用过的模拟地点，便于快速复用。
- 收藏地点：保存常用测试位置。
- 授权与订阅：支持试用、会员授权、订单支付、版本检查和强制更新。

## Features

- Mock GPS location by entering latitude and longitude or selecting a point on the built-in map.
- Route simulation / route mock mode for testing location movement.
- Favorites and recent locations for faster repeated testing.
- Multi-language Android resources.
- Trial, subscription, license check, version check, and payment flow integration.
- In-app privacy policy, user agreement, settings, and update guidance.

## Current Release

- App name: `test Location`
- Version: `1.16`
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
