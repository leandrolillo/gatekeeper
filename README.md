# Gatekeeper

Android app for managing automatic gate opening for authorized contacts via WhatsApp.

## Features

- **Authorized Contacts list** — add contacts with name and phone number
- **Time-based authorization** — assign specific days of the week and a time window (e.g. Mon–Fri 08:00–22:00)
- **WhatsApp monitoring** — listens to incoming WhatsApp notifications using Android's `NotificationListenerService`
- **Keyword matching** — configurable trigger words (default: "open gate", "abrir portão", etc.)
- **Automatic phone call** — calls a configurable gate phone number when a valid trigger is detected
- **30-second cooldown** — prevents duplicate gate opens from the same contact

## Setup

### Required Permissions

| Permission | Purpose |
|---|---|
| `CALL_PHONE` | Make the gate call automatically |
| `READ_CONTACTS` | Optional — for future contact picker integration |
| `BIND_NOTIFICATION_LISTENER_SERVICE` | Read WhatsApp notifications |
| `POST_NOTIFICATIONS` | Show the persistent monitoring notification |

### First Launch

1. Open the app — a **banner** appears if Notification Access isn't granted
2. Tap **Grant** → enable "Gatekeeper" in Android's Notification Access settings
3. Go to **Settings** and enter the **gate phone number** to call
4. Customize **trigger keywords** as needed
5. Add authorized contacts with their timeframes

## How It Works

```
WhatsApp message received
        │
        ▼
Is sender in authorized contacts? ──No──▶ Ignore
        │ Yes
        ▼
Is current time within contact's timeframe? ──No──▶ Ignore
        │ Yes
        ▼
Does message contain a trigger keyword? ──No──▶ Ignore
        │ Yes
        ▼
Is cooldown expired (30s)? ──No──▶ Ignore
        │ Yes
        ▼
📞 Call gate phone number
```

## Tech Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **Room** — local SQLite database for contacts
- **Hilt** — dependency injection
- **DataStore Preferences** — settings persistence
- **NotificationListenerService** — WhatsApp message interception
- MVVM architecture

## Building

```bash
./gradlew assembleDebug
```

Install on device:
```bash
./gradlew installDebug
```

## Notes

- Contact matching is done by **display name** as shown in WhatsApp notifications — ensure the name in Gatekeeper matches exactly what WhatsApp shows (usually the contact's saved name)
- The app uses a **foreground service** with a persistent notification for reliable background operation
- WhatsApp Business (`com.whatsapp.w4b`) is also supported
