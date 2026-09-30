# Eccian2.0

> An Android-based educational resource platform for organizing academic notes, previous-year question papers, and student updates in one place.

**Status:** Work in Progress
**Platform:** Android
**Language:** Java

---

## Overview

Eccian2.0 is an Android application originally developed for students of **Ewing Christian College**, with the goal of making academic resources easier to access and organize.

The application brings commonly used study resources into a single platform, allowing students to navigate academic material by **academic year and subject**, access PDF-based notes and previous-year question papers, and view academic notifications.

Although the initial use case was designed around a specific college environment, the underlying structure can be adapted for other educational institutions.

---

## Problem Statement

Students often rely on resources scattered across messaging groups, cloud storage, and physical documents. Finding the right notes or previous-year question paper can therefore become unnecessarily time-consuming.

Eccian2.0 was developed to explore a centralized approach where academic resources can be organized according to:

**Resource Type → Academic Year → Subject → Document**

This structure makes it easier for students to find and access the material they need.

---

## Features

### Academic Resources

* Browse academic resources by academic year and subject.
* Access study notes in PDF format.
* Access previous-year question papers through the same resource workflow.
* Navigate from year selection to subject selection before accessing resources.

### PDF Management

* Upload PDF resources from an Android device.
* Store PDF files using Firebase Storage.
* Store document metadata and download references using Firebase Realtime Database.
* Retrieve available PDF resources dynamically.
* Download selected PDF files to the device.

### Navigation

* Bottom navigation for the application's primary sections.
* Android Navigation Component for fragment navigation.
* Dedicated sections for:

  * Home
  * Notes
  * Previous-Year Question Papers
  * Notifications

### Notifications

* Display academic announcements through a RecyclerView-based interface.

### Android UI

* XML-based layouts.
* View Binding for accessing UI components.
* Material Components for the interface.
* RecyclerView for list-based content.

---

## Application Flow

The core academic-resource workflow follows:

```text
Student
   │
   ▼
Select Resource Type
   │
   ├── Notes
   │
   └── Previous-Year Question Papers
            │
            ▼
      Select Academic Year
            │
            ▼
        Select Subject
            │
            ▼
       Access PDF Resources
```

The resource-management flow is:

```text
PDF selected on device
        │
        ▼
 Firebase Storage
        │
        ▼
  Download URL
        │
        ▼
Firebase Realtime Database
        │
        ▼
 Resource Metadata
```

---

## Tech Stack

| Technology                       | Purpose                        |
| -------------------------------- | ------------------------------ |
| **Java**                         | Application development        |
| **Android SDK**                  | Android application platform   |
| **XML**                          | UI layouts                     |
| **View Binding**                 | Type-safe access to views      |
| **Android Navigation Component** | Screen and fragment navigation |
| **Material Components**          | UI components                  |
| **RecyclerView**                 | List-based interfaces          |
| **Firebase Realtime Database**   | Resource metadata              |
| **Firebase Storage**             | PDF file storage               |
| **Gradle**                       | Build system                   |
| **JUnit**                        | Unit testing                   |
| **Espresso**                     | Android UI testing             |

---

## Architecture

The application follows a lightweight Android architecture built around Activities, Fragments, ViewModels, and Firebase services.

```text
┌───────────────────────────────┐
│           Android UI          │
│                               │
│ Activities + Fragments        │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│       Application Logic       │
│                               │
│ ViewModels + UI Controllers   │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│            Firebase           │
│                               │
│ Realtime Database + Storage   │
└───────────────────────────────┘
```

The current implementation is intentionally relatively simple and does not yet follow a fully separated repository/domain architecture.

---

## Firebase Architecture

Eccian2.0 uses two Firebase services for academic resources:

### Firebase Realtime Database

Used to store information associated with uploaded resources, including document names and download references.

### Firebase Storage

Used to store the actual PDF files.

The application uses the selected resource type, academic year, and subject to organize and retrieve resources.

> **Note:** The Firebase Storage path structure is still being reviewed and will be documented in more detail once the upload and retrieval paths are standardized.

---

## Project Structure

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── com/example/project20/
        │       ├── MainActivity.java
        │       ├── SplashActivity.java
        │       ├── putPDF.java
        │       ├── ui/
        │       │   ├── dashboard/
        │       │   ├── home/
        │       │   ├── nextphase/
        │       │   ├── notes/
        │       │   └── notifications/
        │       └── ...
        │
        └── res/
            ├── layout/
            ├── navigation/
            ├── menu/
            └── values/
```

---

## Getting Started

### Prerequisites

Before running the project, make sure you have:

* Android Studio
* Android SDK
* JDK compatible with the project's Gradle/Android configuration
* A Firebase project
* An Android device or emulator

### Installation

Clone the repository:

```bash
git clone <repository-url>
```

Open the project in Android Studio and allow Gradle to synchronize the project.

### Firebase Configuration

The application requires Firebase configuration for Realtime Database and Firebase Storage.

Add your Firebase configuration file:

```text
app/google-services.json
```

> Do not commit credentials or sensitive Firebase configuration to a public repository without first reviewing the Firebase project's security configuration and access rules.

### Running the Application

1. Open the project in Android Studio.
2. Configure Firebase.
3. Connect an Android device or start an emulator.
4. Build the project.
5. Run the application.

---

## Screenshots

Screenshots will be added here as the project documentation is finalized.

Example:

| Home         | Notes        |
| ------------ | ------------ |
| <img width="720" height="1600" alt="Home" src="https://github.com/user-attachments/assets/dbabab2d-1f77-4b0a-a9f5-e25fd637de0d" />
 | <img width="720" height="1600" alt="notes" src="https://github.com/user-attachments/assets/07c53b7c-9267-4ef2-b5d0-28a68321046b" />
 |

| Subject Selection | Notifications |
| ----------------- | ------------- |
| <img width="720" height="1600" alt="subject" src="https://github.com/user-attachments/assets/9f15d2db-5503-4974-8099-608237e38ed1" /> |  <img width="720" height="1600" alt="Notification" src="https://github.com/user-attachments/assets/96aa465b-8f48-4bd6-ae8e-d3b03c204bd1" />  |

---

## Current Status

**Work in Progress**

The current implementation contains the core academic-resource workflow, including:

* Application navigation
* Academic year selection
* Subject selection
* PDF upload
* Firebase-based resource storage
* PDF retrieval
* PDF download
* Notification display

The application is currently better described as a **working prototype / ongoing project** rather than a production-ready institutional platform.

---

## Known Limitations

Some parts of the current implementation are still being improved.

* Firebase storage and retrieval paths need to be standardized.
* Data and Firebase operations are currently handled directly in some Activities rather than through a fully separated data layer.
* Some notification content is currently defined locally rather than being retrieved dynamically.
* Automated test coverage needs to be expanded.
* Firebase security configuration requires further review before production deployment.
* The UI and information architecture can be further refined for scalability across multiple institutions.

---

## Future Improvements

Planned improvements include:

* [ ] Improve the application's architecture and separation of concerns.
* [ ] Introduce a dedicated repository/data layer.
* [ ] Improve Firebase security rules.
* [ ] Replace locally defined notifications with a dynamic notification system.
* [ ] Improve document organization and search.
* [ ] Add stronger automated test coverage.
* [ ] Improve UI/UX and accessibility.
* [ ] Support a broader range of educational institutions.
* [ ] Add authentication and role-based access where required.

---

## What I Learned

Working on Eccian2.0 provided practical experience with Android application development and cloud-backed mobile applications.

Key areas explored during development include:

* Android application architecture
* Java-based Android development
* Fragment navigation
* View Binding
* RecyclerView
* Firebase Realtime Database
* Firebase Storage
* File upload and download workflows
* Handling PDF resources on Android
* Designing an academic resource hierarchy
* Connecting an Android application to a cloud backend

The project also highlighted the importance of separating UI logic from data operations as an application grows, which is an area I am continuing to improve.

---

## Testing

The project includes dependencies for:

* JUnit
* AndroidX testing
* Espresso

Testing coverage is currently limited and will be expanded as the application architecture and feature set mature.

---

## License

This project is currently maintained as a personal/student development project.

License information will be added when the project's distribution terms are finalized.

---

## Author

**Jayati Rai**

Android Developer | Java | Kotlin | Firebase

[GitHub](github-profile-url) · [LinkedIn](linkedin-profile-url)
