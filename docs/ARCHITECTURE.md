# Architecture Documentation

Kaushalya Karnataka follows the **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** design pattern. This ensures the project is scalable, maintainable, and easy to test.

## 🏗️ High-Level Architecture

The project is divided into several layers:

1.  **UI Layer (Jetpack Compose)**:
    -   **Screens**: Composable functions representing full-screen UI (e.g., `DiscoveryScreen`, `ProfileEditorScreen`).
    -   **Components**: Reusable UI widgets (e.g., `WorkerCard`, `StarRating`).
    -   **Navigation**: Handles the app flow using Jetpack Navigation Compose.

2.  **Presentation Layer (ViewModel)**:
    -   Manages UI state using `StateFlow`.
    -   Communicates with the Repository layer to fetch or update data.
    -   Survives configuration changes.

3.  **Domain/Data Layer (Repository & Models)**:
    -   **Models**: Pure Kotlin data classes representing the domain entities (`Worker`, `Service`, `Review`).
    -   **Repository**: Acts as a single source of truth. It abstracts the data source (Firebase Firestore) from the rest of the app.
    -   **Utility**: Pure logic functions like `WorkerSearch` which are framework-independent and unit-testable.

## 🔄 Data Flow

1.  The **UI** observes a `StateFlow` from the **ViewModel**.
2.  The **ViewModel** requests data from the **Repository**.
3.  The **Repository** fetches data from **Firebase Firestore** (or returns local sample data as a fallback).
4.  Data is mapped to domain **Models** and sent back to the **ViewModel**.
5.  The **ViewModel** updates the **UI State**, which triggers a recomposition in the **UI**.

## 🛠️ Key Technologies

-   **Jetpack Compose**: Modern declarative UI.
-   **Kotlin Coroutines & Flow**: For asynchronous programming and reactive data streams.
-   **Firebase Firestore**: Real-time NoSQL database for cloud storage.
-   **Firebase Auth**: Secure user authentication.
-   **Coil**: Image loading for professional worker profiles.

## 🧪 Testing Strategy

-   **Unit Tests**: Located in `app/src/test`. These test the business logic in ViewModels and Utility classes (e.g., `WorkerSearchTest`).
-   **Instrumentation Tests**: Located in `app/src/androidTest`. These test the UI components and integration.
