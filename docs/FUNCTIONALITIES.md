# Functionalities (Pill Tracker)

## Core use-cases

### 1) Daily pill reminder
- User sets a **daily reminder time** (e.g., 08:00).
- App schedules the next reminder using **WorkManager**.
- At the scheduled time, a notification is shown.

### 2) Confirmation that the pill is taken
User can confirm in two ways:
- **Inside the app**: Tap “Mark as taken”.
- **From the notification**: Tap “Taken”.

When confirmed, the app:
- Stores a single “dose taken” record for **today** (one per day).
- Displays “Taken at HH:mm” on the Home screen.
- Schedules the next reminder for **tomorrow** (so you don’t get reminded again today).

### 3) Snooze
- From the notification, user can tap **Snooze 15 min**.
- App schedules a one-time “snooze reminder” notification after 15 minutes.
- Snooze does **not** affect the regular daily schedule.

### 4) History and streak
- History screen lists recent days with:
  - Date
  - Status (Taken / Not taken)
  - Time taken (if taken)
- Streak is computed as consecutive days (including today if taken).

## Settings

- Pill label (e.g., “Blood pressure pill”)
- Daily reminder time (hour + minute)
- Reminders enabled/disabled

## Data & privacy

- All data remains on-device.
- No analytics, no sign-in, no network calls.

## Known limitations (by design)
- The schedule is driven by WorkManager, so the reminder time can be **approximate** on some devices due to OS power management.
  - If you want exact, “alarm-clock precise” reminders, the scheduling can be upgraded to AlarmManager + exact alarms.

