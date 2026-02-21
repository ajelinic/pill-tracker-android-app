# Pill Tracker (Android, Kotlin + Jetpack Compose)

A simple, local-only pill reminder + confirmation app for daily blood-pressure medication.

> Medical note: This app is a reminder + logging tool. It does not provide medical advice. Follow your clinician’s instructions.

## Features

- **Daily reminder notification** at a configurable time
  - Notification actions: **Taken** (logs immediately) and **Snooze 15 min**
- **One-tap confirmation** inside the app (“Mark as taken”)
- **Today status** (Taken / Not taken) + taken time
- **Adherence history** list (last 60 days by default)
- **Streak** (consecutive days taken)
- **Local storage only**
  - Settings stored in **DataStore**
  - Confirmations stored in **Room** (SQLite)
- **No account, no network calls**

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- Room
- DataStore Preferences
- WorkManager (schedules the next reminder as a one-time task)

## Quick start (Android Studio)

1. Open Android Studio → **Open** → select this folder.
2. Let Gradle sync and download dependencies.
3. Run on an emulator/device (Android 8.0+ / API 26+).

If you see any build errors related to versions, adjust versions in `gradle/libs.versions.toml`.

## Permissions

- Android 13+ (API 33+): the app will request **POST_NOTIFICATIONS** at runtime.

## Repository structure

- `app/src/main/java/com/pilltracker/...` → app source
- `docs/FUNCTIONALITIES.md` → full functionality documentation
