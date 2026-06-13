# Contributing to test Location

Thank you for your interest in contributing to test Location! 🎉

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 8 or later
- Android SDK 33 (API level 33)
- Android device or emulator running Android 5.0+ (API 21+)

### Setting Up Development Environment

1. **Fork** this repository
2. **Clone** your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/test-location.git
   cd test-location
   ```
3. Open the project in Android Studio
4. Sync Gradle and build the project

## Development Workflow

### Branching Strategy

- `main` — stable release branch
- `develop` — development branch
- Feature branches: `feature/your-feature-name`
- Bug fix branches: `fix/issue-description`

### Commit Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add route simulation speed control
fix: resolve mock location provider crash on Android 14
docs: update API documentation
refactor: extract location helper to utility class
chore: update gradle plugin version
```

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use 4 spaces for indentation (no tabs)
- Keep methods concise and focused
- Add Javadoc comments for public APIs
- Use meaningful variable and method names

### Pull Request Process

1. Create a feature branch from `develop`
2. Make your changes
3. Test on a real device if possible
4. Update documentation if needed
5. Submit a Pull Request to `develop` branch
6. Ensure CI checks pass
7. Request review from maintainers

## Reporting Issues

### Bug Reports

Please include:
- Device model and Android version
- Steps to reproduce
- Expected vs actual behavior
- Screenshots or screen recordings if applicable
- Logcat output for crashes

### Feature Requests

Please include:
- Use case description
- Proposed solution
- Alternative approaches considered

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help others learn and grow

## Contact

- Email: aiaiks720@gmail.com
- Issues: [GitHub Issues](https://github.com/aiaiks720/test-location/issues)

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
