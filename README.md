# 📞 Android Spam Call Blocker

Android application for spam call detection and automatic call blocking, built using modern Android development practices.

This app allows users to search phone numbers, identify potential spam, and automatically block unwanted calls using Android’s Call Screening Service.

---

## 🚀 Features

- 🔍 Search phone numbers and detect spam
- 🚫 Automatically block incoming spam calls
- 🧾 Store search history locally
- 📵 View blocked calls history
- 📊 Display locally stored spam numbers
- 🌙 Light/Dark mode support
- ⚡ Real-time UI updates with reactive state

---

## 🧱 Architecture

This project follows a **clean MVVM architecture**:

- **UI (Jetpack Compose)**  
  Handles user interaction and displays state

- **ViewModel**  
  Manages UI state and business logic

- **Repository Layer**  
  Abstracts data sources

- **DAO (Room)**  
  Handles database access

- **Database (Room)**  
  Local persistence layer

- **Service Layer**  
  Intercepts and blocks calls using Android system APIs

---

## 🧠 Design Patterns Used

- **MVVM (Model-View-ViewModel)**  
- **Repository Pattern**  
- **Dependency Injection (Hilt)**  
- **Observer / Reactive Programming (StateFlow & Flow)**  
- **DAO Pattern (Room)**  
- **Singleton (Database & Repositories)**  

---

## 🛠 Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Room Database (Room3 + SQLite Driver)**
- **Hilt (Dependency Injection)**
- **Coroutines + Flow**
- **Android CallScreeningService**
- **JUnit (Unit Testing)**

---

## 🗄 Database

The app uses **Room** for local data persistence with the following tables:

- `spam_numbers` → known spam numbers  
- `search_history` → user search history  
- `blocked_calls` → automatically blocked calls  

### Example DAO methods:

- Insert spam numbers  
- Check if a number is spam  
- Retrieve history as reactive streams  

Example:
- Spam numbers are queried and inserted via DAO methods such as  
  `getAllSpamNumbers()` and `insertAll()` :contentReference[oaicite:0]{index=0}  

- Search history is observed using `Flow` for real-time updates :contentReference[oaicite:1]{index=1}  

- Blocked calls are stored and retrieved reactively :contentReference[oaicite:2]{index=2}  

---

## ⚙️ Database Pre-population

The database is automatically pre-populated with sample spam numbers on first launch.

This ensures:
- Immediate usability
- Easy testing without manual data entry

---

## 📞 Call Screening Service

The app integrates with Android’s **CallScreeningService** to:

- Intercept incoming calls
- Check against spam database
- Automatically reject spam calls
- Save blocked calls to history

---

## 🧪 Testing

The project includes **unit tests** for:

- ViewModel logic
- Repository behavior
- Input validation
- State handling

This ensures:
- Reliability
- Maintainability
- Safer future changes

---

## ▶️ Getting Started

### Requirements

- Android Studio (latest version recommended)
- Android SDK installed
- Kotlin support enabled

---

### Installation

```bash
git clone https://github.com/NunoRebelo04/SpamCallIdentifier
