# Kaushalya Karnataka

**Empowering Skilled Workers Across Karnataka — A Native Android Platform**

Kaushalya Karnataka is a mobile-first platform designed to connect skilled blue-collar workers (electricians, plumbers, carpenters, painters, masons, AC technicians, welders) with local customers across Karnataka, India. Workers create professional profiles showcasing their services, portfolio, and pricing, while citizens can discover, filter, review, and hire trusted professionals in their neighborhood.

---

## Problem Statement

In Karnataka, thousands of skilled laborers — electricians, plumbers, carpenters, and others — struggle to find consistent work and grow their client base. At the same time, citizens often have difficulty locating reliable, verified professionals for home and commercial services. There is no unified, accessible digital platform that bridges this gap at a local, district-level scale.

**Kaushalya Karnataka** solves this by giving every skilled worker a digital identity and making them discoverable to their community.

---

## Features

### For Skilled Workers (Professionals)
- **Google Sign-In Authentication** — Secure login via Firebase Authentication with Google Credential Manager
- **Professional Profile Builder** — Create and manage a complete business identity with name, specialty category, bio, skills, and area of operation
- **Service Listing Manager** — Add, edit, and remove offered services with flexible pricing (fixed or starting-at)
- **Portfolio Gallery** — Upload photos of completed work to build trust with potential customers
- **Business Dashboard** — View total leads, average rating, verification status at a glance
- **Customer Lead Inbox** — Receive and manage hire requests with swipe-to-dismiss, call-now, and mark-as-contacted actions
- **Profile Sharing** — Share your professional profile via Android's native share sheet
- **Real-time Sync** — All changes sync instantly to Firebase Firestore for all users

### For Citizens (Customers)
- **Worker Discovery Feed** — Browse verified professionals with rich card-based UI
- **Category Filtering** — Filter by skill category (Electrician, Plumber, Carpenter, Painter, Mason, AC Technician, Welder)
- **Location-Based Discovery** — Filter workers by operating district across Karnataka
- **Advanced Filters** — Set minimum rating and maximum budget constraints via a bottom sheet
- **Search** — Real-time text search across worker names and categories
- **Recently Active Section** — See professionals who recently updated their profiles
- **Worker Detail View** — Full profile with services, portfolio gallery, reviews, and hire button
- **Hire Request Dialog** — Submit name, phone, and message to request a worker's services
- **Review System** — Leave star ratings and text reviews for professionals

### UX & Design
- **Material 3 Design System** — Fully themed with a warm orange brand palette
- **Dark Mode Support** — Automatic adaptation to system dark/light theme
- **Edge-to-Edge UI** — Modern immersive layout using system bar insets
- **Smooth Animations** — Animated transitions between screens and tabs
- **Shimmer Loading States** — Skeleton loading cards while data is being fetched
- **Haptic Feedback** — Tactile feedback on important interactions
- **Pull-to-Refresh** — Swipe down to reload the discovery feed
- **Empty State Illustrations** — Helpful prompts when no data is available

---

## Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Declarative UI framework |
| **Material 3 (Material You)** | Design system and theming |
| **Firebase Authentication** | Google Sign-In for user authentication |
| **Firebase Firestore** | Cloud NoSQL database for real-time data sync |
| **Google Credential Manager** | Modern Android credential/login API |
| **Jetpack Navigation Compose** | Screen navigation with arguments and animations |
| **Coil** | Async image loading and caching |
| **Kotlin Coroutines & Flow** | Asynchronous programming and reactive state |
| **ViewModel + StateFlow** | MVVM architecture with lifecycle-aware state management |
| **Gradle Version Catalog** | Centralized dependency management via `libs.versions.toml` |

---

## Project Structure

```
KaushalyaKarnataka/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/kaushalya_karnataka/
│   │   │   ├── MainActivity.kt              # Entry point with edge-to-edge setup
│   │   │   ├── components/                   # Reusable UI components
│   │   │   │   ├── CategoryChip.kt           # Filter chip for categories
│   │   │   │   ├── EmptyState.kt             # Empty state placeholder
│   │   │   │   ├── HireDialog.kt             # Hire request form dialog
│   │   │   │   ├── ReviewDialog.kt           # Review submission dialog
│   │   │   │   ├── ReviewItem.kt             # Individual review display
│   │   │   │   ├── ServiceCard.kt            # Service item card
│   │   │   │   ├── ShimmerWorkerCard.kt       # Shimmer loading skeleton
│   │   │   │   ├── StarRating.kt             # Star rating display
│   │   │   │   └── WorkerCard.kt             # Worker profile card
│   │   │   ├── data/                         # Data layer
│   │   │   │   ├── SampleData.kt             # Sample/mock data for development
│   │   │   │   └── WorkerRepository.kt       # Firebase Firestore data access
│   │   │   ├── models/                       # Data models
│   │   │   │   └── Worker.kt                 # Worker, Service, Review, HireRequest models
│   │   │   ├── navigation/                   # Navigation graph
│   │   │   │   └── NavGraph.kt               # Screen routes and navigation setup
│   │   │   ├── screens/                      # Full-screen composables
│   │   │   │   ├── SplashScreen.kt           # Animated splash with auth check
│   │   │   │   ├── LoginScreen.kt            # Google Sign-In screen
│   │   │   │   ├── MainScreen.kt             # Bottom navigation host
│   │   │   │   ├── DiscoveryScreen.kt        # Worker discovery feed
│   │   │   │   ├── WorkerDetailScreen.kt     # Detailed worker profile view
│   │   │   │   └── ProfileEditorScreen.kt    # Business profile editor (4 tabs)
│   │   │   ├── ui/theme/                     # Material 3 theming
│   │   │   │   ├── Color.kt                  # Color palette definitions
│   │   │   │   ├── Theme.kt                  # Light/Dark color schemes
│   │   │   │   └── Type.kt                   # Typography configuration
│   │   │   └── viewmodel/                    # ViewModels (MVVM)
│   │   │       ├── AuthViewModel.kt          # Authentication state
│   │   │       ├── SplashViewModel.kt        # Splash screen routing logic
│   │   │       ├── MainViewModel.kt          # Main screen state & lead notifications
│   │   │       ├── DiscoveryViewModel.kt     # Discovery feed filtering & search
│   │   │       ├── WorkerDetailViewModel.kt  # Worker detail data loading
│   │   │       └── ProfileEditorViewModel.kt # Profile editing state management
│   │   ├── res/                              # Android resources
│   │   │   ├── drawable/                     # App icons and vector assets
│   │   │   ├── mipmap-*/                     # Launcher icons (all densities)
│   │   │   ├── values/                       # Colors, strings, themes
│   │   │   └── xml/                          # Backup and data extraction rules
│   │   └── AndroidManifest.xml               # App manifest with permissions
│   └── build.gradle.kts                      # App module build configuration
├── gradle/
│   └── libs.versions.toml                    # Centralized dependency versions
├── build.gradle.kts                          # Root project build file
├── settings.gradle.kts                       # Project settings
├── gradle.properties                         # Gradle configuration properties
├── gradlew / gradlew.bat                     # Gradle wrapper scripts
└── README.md                                 # This file
```

---

## Architecture

The project follows **MVVM (Model-View-ViewModel)** architecture with clear separation of concerns:

```
┌─────────────────────────────────────────────────────┐
│                    UI Layer                         │
│  Screens (Composable) ◄── Components (Reusable)    │
│         │                                           │
│         ▼                                           │
│    ViewModels (StateFlow)                           │
│         │                                           │
│         ▼                                           │
│    Data Layer                                       │
│  WorkerRepository ◄── Firebase Firestore            │
│                    ◄── SampleData (dev fallback)    │
│         │                                           │
│         ▼                                           │
│    Models (Data Classes)                            │
│  Worker, Service, Review, HireRequest, Portfolio    │
└─────────────────────────────────────────────────────┘
```

---

## Installation & Setup

### Prerequisites
- **Android Studio** Ladybug or later (AGP 9.0+)
- **JDK 11** or higher
- **Android SDK** with API level 24+ (Android 7.0 Nougat minimum)
- A **Firebase project** with Authentication and Firestore enabled

### Steps to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/challateja/Kaushalya-Karnataka.git
   cd Kaushalya-Karnataka
   ```

2. **Open in Android Studio**
   - Open Android Studio → File → Open → Select the cloned project folder
   - Wait for Gradle sync to complete

3. **Configure Firebase** (already included)
   - The project includes a `google-services.json` file pre-configured for the app
   - If using your own Firebase project, replace `app/google-services.json` with your own

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or use Android Studio's **Run** button (▶) to install on a connected device/emulator.

5. **Run on Device/Emulator**
   - Target SDK: API 36 (Android 16)
   - Minimum SDK: API 24 (Android 7.0)
   - Ensure Google Play Services is available on the device for Google Sign-In

---

## How to Use

1. **Launch the App** — The animated splash screen checks your authentication status
2. **Sign In** — Tap "Join as Professional" to sign in with your Google account
3. **Discover Tab** — Browse, search, and filter skilled workers in your area
4. **Tap a Worker Card** — View detailed profile, services, portfolio, and reviews
5. **Hire a Worker** — Fill the hire request form with your name, phone, and message
6. **Leave a Review** — Rate and review a worker after hiring
7. **My Business Tab** — Build your own professional profile
   - Set up your identity (name, category, location, skills, bio)
   - Add work services with pricing
   - Upload portfolio photos
   - Manage customer inquiries from the Leads tab
8. **Save & Share** — Save your profile (syncs to all users) and share via any app

---

## Screenshots

> The app features a modern Material 3 design with both light and dark mode support.

| Splash Screen | Login Screen | Discovery Feed |
|:---:|:---:|:---:|
| Animated brand splash with gradient background | Google Sign-In with professional branding | Filterable worker cards with search and categories |

| Worker Detail | Profile Editor | Leads Inbox |
|:---:|:---:|:---:|
| Full profile with services, portfolio, and reviews | 4-tab business hub with service & portfolio management | Swipe-to-dismiss customer inquiries with call actions |

---

## Key Design Decisions

- **Firebase Firestore over Room**: Chosen for real-time cross-device sync — when a worker updates their profile, all citizens see changes instantly
- **Jetpack Compose over XML**: Modern declarative UI enables rapid iteration and cleaner code
- **Credential Manager over legacy Sign-In**: Future-proof authentication using Google's latest identity APIs
- **StateFlow over LiveData**: Better Kotlin coroutine integration and null-safety
- **Coil over Glide/Picasso**: Kotlin-first image loading with native Compose support

---

## Future Improvements

- [ ] Push notifications for new hire requests via Firebase Cloud Messaging
- [ ] In-app chat between workers and customers
- [ ] GPS-based location auto-detection
- [ ] Worker verification badge workflow with document upload
- [ ] Payment integration for service bookings
- [ ] Multi-language support (Kannada, Hindi, English)
- [ ] Analytics dashboard for professionals
- [ ] Image compression and cloud storage via Firebase Storage
- [ ] Offline mode with local caching

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is developed as an academic submission for the Kaushalya Karnataka initiative. All rights reserved.

---

## Contact

- **Developer**: Challa Teja
- **Repository**: [github.com/challateja/Kaushalya-Karnataka](https://github.com/challateja/Kaushalya-Karnataka)
