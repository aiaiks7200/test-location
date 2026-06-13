<div align="center">

# 📍 test Location

**Professional Android Mock Location Development & Debugging Tool**

[![Android](https://img.shields.io/badge/Android-5.0%2B-green?logo=android)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-21%2B-blue)](https://developer.android.com/studio/releases/platforms)
[![Version](https://img.shields.io/badge/version-1.13-orange)](https://github.com/aiaiks720/test-location/releases)
[![License](https://img.shields.io/badge/license-MIT-purple)](LICENSE)
[![Language](https://img.shields.io/badge/language-Java-blue?logo=java)](https://www.java.com)

[English](#english) | [中文](#中文) | [日本語](#日本語) | [한국어](#한국어)

</div>

---

<a name="english"></a>
## 🇬🇧 English

### 📖 Overview

**test Location** is a professional Android mock location tool designed for developers, QA engineers, and testers. It provides GPS location simulation capabilities for software development, feature debugging, and application compatibility testing.

> ⚠️ **Disclaimer**: This software is intended **solely** for legitimate development, testing, and debugging purposes. Any use for illegal activities (fake check-ins, ride-hailing fraud, location spoofing for games, etc.) is strictly prohibited and may result in legal consequences.

### ✨ Features

| Feature | Description |
|---------|-------------|
| 🗺️ **Map Selection** | Tap-to-select location on OpenStreetMap (Leaflet.js) |
| 📍 **Fixed Mock** | Set a static mock GPS location |
| 🛤️ **Route Simulation** | Simulate movement between two points with adjustable speed |
| ⏱️ **Speed Control** | Configurable movement speed (1-50 km/h) |
| ⭐ **Favorites** | Save and manage favorite locations |
| 🕐 **Recent Locations** | Quick-start from up to 3 recent mock locations |
| 🌐 **Multi-Language** | English, 中文, 日本語, 한국어, Русский, Français, Deutsch |
| 🔔 **Foreground Service** | Persistent notification during mock operation |
| 🔒 **License System** | Server-side verification with offline cache |
| 💳 **Payment** | Alipay and WeChat Pay subscription support |

### 📸 Screenshots

> *Screenshots coming soon*

### 🚀 Quick Start

#### Prerequisites

- Android 5.0 (API 21) or higher
- Developer Options enabled on your device

#### Installation

**Option 1: Download APK**
```
Download the latest APK from Releases
```

**Option 2: Build from Source**
```bash
git clone https://github.com/aiaiks720/test-location.git
cd test-location
./gradlew assembleDebug
```

#### Setup

1. **Enable Developer Options** on your Android device:
   - Go to `Settings → About Phone`
   - Tap `Build Number` 7 times

2. **Set Mock Location App**:
   - Go to `Settings → Developer Options`
   - Find `Select mock location app`
   - Select `test Location`

3. **Grant Permissions**:
   - Location permission (Fine + Coarse)
   - Notification permission (Android 13+)

4. **Start Using**:
   - Open the app
   - Accept the Terms of Service
   - Tap `Open Map` to select a location
   - Tap `Start Mock` to begin simulation

### 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│                   UI Layer                       │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
│  │  Main    │ │   Map    │ │    Payment       │ │
│  │ Activity │ │ Activity │ │    Activity      │ │
│  └────┬─────┘ └────┬─────┘ └───────┬──────────┘ │
│       │            │               │             │
│  ┌────┴────────────┴───────────────┴──────────┐  │
│  │           TrialManager                      │  │
│  │    (License + Subscription State)           │  │
│  └────────────────────┬───────────────────────┘  │
│                       │                          │
│  ┌────────────────────┴───────────────────────┐  │
│  │       MockLocationService (Foreground)      │  │
│  │    GPS Provider + Network Provider          │  │
│  └────────────────────┬───────────────────────┘  │
└───────────────────────┼──────────────────────────┘
                        │
              ┌─────────┴─────────┐
              │   Android System  │
              │  LocationManager  │
              └───────────────────┘
```

### 📁 Project Structure

```
test-location/
├── app/
│   ├── src/main/
│   │   ├── java/com/test/mocklocation/
│   │   │   ├── MainActivity.java          # Main entry point
│   │   │   ├── MapActivity.java           # Map & location selection
│   │   │   ├── MockLocationService.java   # Core mock location service
│   │   │   ├── PaymentActivity.java       # Subscription payment
│   │   │   ├── SettingsActivity.java      # App settings
│   │   │   ├── TrialManager.java          # Trial & subscription logic
│   │   │   ├── LicenseApiHelper.java      # Server license verification
│   │   │   ├── PaymentApiHelper.java      # Payment API client
│   │   │   ├── PlanApiHelper.java         # Plan pricing API
│   │   │   ├── FavoritesManager.java      # Favorite locations storage
│   │   │   ├── RecentLocationManager.java # Recent locations storage
│   │   │   ├── LocationHelper.java        # Location utility methods
│   │   │   ├── AppProtector.java          # Security protection
│   │   │   ├── SignatureVerifier.java     # APK signature verification
│   │   │   ├── StringObfuscator.java      # String obfuscation
│   │   │   └── MD5Util.java               # MD5 hashing utility
│   │   ├── res/
│   │   │   ├── layout/                    # UI layouts
│   │   │   ├── values/                    # Strings, colors, styles
│   │   │   ├── values-zh/                 # Chinese translations
│   │   │   ├── values-ja/                 # Japanese translations
│   │   │   ├── values-ko/                 # Korean translations
│   │   │   ├── values-ru/                 # Russian translations
│   │   │   ├── values-fr/                 # French translations
│   │   │   ├── values-de/                 # German translations
│   │   │   └── xml/                       # Network security config
│   │   ├── assets/
│   │   │   ├── map.html                   # Map WebView page
│   │   │   ├── map.js                     # Map JavaScript logic
│   │   │   ├── leaflet.js                 # Leaflet.js library
│   │   │   └── leaflet.css                # Leaflet CSS
│   │   └── AndroidManifest.xml
│   ├── build.gradle                       # App-level build config
│   └── proguard-rules.pro                 # ProGuard rules
├── docs/                                  # Technical documentation
│   ├── ARCHITECTURE.md                    # Architecture deep dive
│   ├── API.md                             # Backend API reference
│   ├── SECURITY.md                        # Security mechanisms
│   └── BUILD.md                           # Build instructions
├── build.gradle                           # Project-level build config
├── settings.gradle
├── gradle.properties
├── build.sh                               # Build script
├── CHANGELOG.md
├── CONTRIBUTING.md
├── LICENSE
└── README.md
```

### 📚 Documentation

| Document | Description |
|----------|-------------|
| [Architecture](docs/ARCHITECTURE.md) | Detailed architecture and design patterns |
| [API Reference](docs/API.md) | Backend API endpoints and protocols |
| [Security](docs/SECURITY.md) | Security mechanisms and protections |
| [Build Guide](docs/BUILD.md) | Step-by-step build instructions |

### 🔧 Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 8 |
| UI | Android XML Layouts + WebView (Leaflet.js) |
| Map | OpenStreetMap via Leaflet.js |
| Build | Gradle 7.4.2 + Android Gradle Plugin |
| Min SDK | API 21 (Android 5.0 Lollipop) |
| Target SDK | API 33 (Android 13) |
| Payment | Alipay / WeChat Pay via server API |
| License | Server-side verification with local cache |

### 💰 Pricing

| Plan | Price | Duration |
|------|-------|----------|
| Free Trial | ¥0 | 15 days |
| Monthly | ¥19.8 | 30 days |
| Yearly | ¥168 | 365 days |

### 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 📧 Contact

- **Email**: aiaiks720@gmail.com
- **Website**: https://dw.locati.xyz
- **Admin**: https://dw.locati.xyz/admin

---

<a name="中文"></a>
## 🇨🇳 中文

### 📖 简介

**test Location** 是一款专业的 Android 模拟定位工具，专为开发者、测试工程师设计。提供 GPS 位置模拟功能，用于软件开发、功能调试和应用兼容性测试。

> ⚠️ **免责声明**：本软件**仅限**用于合法的开发、测试和调试目的。严禁用于任何非法活动（虚假打卡、网约车作弊、游戏位置伪造等），违者可能承担法律责任。

### ✨ 功能特性

| 功能 | 说明 |
|------|------|
| 🗺️ **地图选点** | 在 OpenStreetMap（Leaflet.js）上点击选点 |
| 📍 **定点模拟** | 设置静态模拟 GPS 位置 |
| 🛤️ **路线模拟** | 模拟两点之间的移动，可调速度 |
| ⏱️ **速度控制** | 可配置移动速度（1-50 km/h） |
| ⭐ **收藏夹** | 保存和管理收藏的位置 |
| 🕐 **最近地点** | 快速启动最近 3 个模拟地点 |
| 🌐 **多语言** | 英文、中文、日文、韩文、俄文、法文、德文 |
| 🔔 **前台服务** | 模拟运行时持续通知 |
| 🔒 **授权系统** | 服务端验证 + 离线缓存 |
| 💳 **支付** | 支持支付宝和微信支付 |

### 🚀 快速开始

#### 环境要求

- Android 5.0（API 21）或更高版本
- 已开启开发者选项

#### 安装方式

**方式一：下载 APK**
```
从 Releases 页面下载最新 APK
```

**方式二：从源码构建**
```bash
git clone https://github.com/aiaiks720/test-location.git
cd test-location
./gradlew assembleDebug
```

#### 使用步骤

1. **开启开发者选项**：
   - 进入 `设置 → 关于手机`
   - 连续点击 `版本号` 7 次

2. **设置模拟位置应用**：
   - 进入 `设置 → 开发者选项`
   - 找到 `选择模拟位置信息应用`
   - 选择 `test Location`

3. **授予权限**：
   - 定位权限（精确 + 粗略）
   - 通知权限（Android 13+）

4. **开始使用**：
   - 打开应用
   - 同意使用条款
   - 点击 `打开地图` 选择位置
   - 点击 `启动模拟` 开始模拟

### 💰 价格方案

| 方案 | 价格 | 时长 |
|------|------|------|
| 免费试用 | ¥0 | 15 天 |
| 月度会员 | ¥19.8 | 30 天 |
| 年度会员 | ¥168 | 365 天 |

### 📧 联系方式

- **邮箱**: aiaiks720@gmail.com
- **官网**: https://dw.locati.xyz

---

<a name="日本語"></a>
## 🇯🇵 日本語

### 📖 概要

**test Location** は、開発者やQAエンジニアのために設計されたプロフェッショナルなAndroidモックロケーションツールです。ソフトウェア開発、機能デバッグ、アプリケーション互換性テストのためのGPS位置シミュレーション機能を提供します。

### ✨ 機能

- 🗺️ OpenStreetMap（Leaflet.js）上でのタップ選択
- 📍 固定モック位置設定
- 🛤️ 2点間のルートシミュレーション（速度調整可能）
- ⭐ お気に入り地点の保存
- 🌐 7言語対応
- 🔒 サーバーベースのライセンス認証

### 🚀 クイックスタート

1. Android 5.0以上が必要
2. 開発者オプションを有効にする
3. 「モック位置情報アプリ」にtest Locationを設定
4. 位置権限を付与
5. アプリを開いて使用開始

---

<a name="한국어"></a>
## 🇰🇷 한국어

### 📖 개요

**test Location** 은 개발자와 QA 엔지니어를 위해 설계된 전문 안드로이드 목 위치 도구입니다. 소프트웨어 개발, 기능 디버깅, 애플리케이션 호환성 테스트를 위한 GPS 위치 시뮬레이션 기능을 제공합니다.

### ✨ 기능

- 🗺️ OpenStreetMap(Leaflet.js)에서 탭으로 위치 선택
- 📍 고정 목 위치 설정
- 🛤️ 두 지점 간 경로 시뮬레이션 (속도 조절 가능)
- ⭐ 즐겨찾기 위치 저장
- 🌐 7개 언어 지원
- 🔒 서버 기반 라이선스 인증

### 🚀 빠른 시작

1. Android 5.0 이상 필요
2. 개발자 옵션 활성화
3. "모의 위치 앱"에 test Location 설정
4. 위치 권한 부여
5. 앱을 열어 사용 시작

---

<div align="center">

**Made with ❤️ by test Location Team**

[⬆ Back to Top](#-test-location)

</div>
