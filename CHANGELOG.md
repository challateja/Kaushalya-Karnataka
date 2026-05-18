# Changelog

All notable changes to **Kaushalya Karnataka** will be documented in this file.

## [1.1.0] - 2024-11-20
### Added
- **Security**: Defined production-ready `firestore.rules` for the workers collection.
- **Architecture**: Implemented a unified `Resource` wrapper for standardized error and loading state management.
- **Testing**: Added unit tests for `WorkerSearch` logic to verify filtering accuracy.
- **CI/CD**: Configured GitHub Actions workflow for automated build and test verification on every push.
- **Documentation**: Added `ARCHITECTURE.md`, `CONTRIBUTING.md`, and `LICENSE`.

### Improved
- **UI/UX**: Migrated all hardcoded strings to `strings.xml` for better maintainability and localization.
- **System UI**: Refined `MainActivity` with advanced Edge-to-Edge integration and dynamic theme-aware system bars.
- **README**: Completely overhauled documentation with screenshots, problem statement, and evaluation compliance mapping.

## [1.0.0] - 2024-11-15
### Added
- Initial implementation of the Professional Discovery Feed.
- Firebase Firestore integration for real-time worker updates.
- Google Sign-In authentication.
- Worker Profile Editor and Service Management.
- Material 3 theming with custom brand palette.
