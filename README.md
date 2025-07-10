🛠️ Tech Stack & library

Minimum Requirements

Minimum SDK: 24 (Android 7.0 Nougat)

Language: Kotlin

Architecture

MVVM (Model-View-ViewModel):
Separates UI, business logic, and data layers for better maintainability and testability.

Repository Pattern:
Acts as a mediator between data sources (local DB, network, etc.) and business logic, ensuring consistent data management.

Core Libraries

Jetpack Compose
Android’s modern declarative UI toolkit for building native interfaces with less code.

(androidx.compose.*, material3, foundation, ui, animation, tooling.preview, material.icons.extended, constraintlayout.compose)

Lifecycle
Observes Android component lifecycles and manages UI state accordingly.
(androidx.lifecycle.*)

ViewModel
Stores and manages UI-related data in a lifecycle-conscious way, surviving configuration changes.
(androidx.lifecycle.viewmodel.*)

Navigation Compose
Simplifies navigation and deep linking between composables in Jetpack Compose.
(androidx.navigation.compose)

Room

Provides a type-safe abstraction layer over SQLite for local database access.
(androidx.room.*)

Hilt

Standardizes dependency injection, automating object creation and improving testability.
(hilt, hilt.android, androidx.hilt.navigation.compose, kapt(hilt.compiler))

Retrofit2

Type-safe HTTP client for communicating with REST APIs.
(retrofit, converter.gson)

OkHttp3

High-performance HTTP client for network communication.
(okhttp)

Gson

Library for serializing and deserializing JSON data.
(gson)

Coil

Fast, lightweight image loading library optimized for Jetpack Compose.
(coil.compose)

Coroutines & Flow

Simplifies asynchronous programming and stream-based data updates.
(androidx.lifecycle.runtime.ktx, androidx.room.ktx)

ConstraintLayout Compose

Enables flexible and complex UI layouts using constraints in Compose.
(androidx.constraintlayout.compose)

Open-Source Libraries Used

Jetpack Compose

AndroidX Lifecycle

AndroidX Navigation Compose

Room

Hilt

Retrofit2

OkHttp3

Gson

Coil

ConstraintLayout Compose
