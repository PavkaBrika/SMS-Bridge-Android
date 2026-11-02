# SMS Modem 📱

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="SMS Modem app icon" width="144" />
</p>

<p align="center"><strong>Turn an Android phone into an SMS gateway for your server.</strong></p>

SMS Modem connects your backend to a phone's SMS capabilities. Send messages through an HTTP API running on the device, and forward incoming SMS messages to your own server.

## ✨ What the app does

- 📤 Send SMS messages from the phone through an authenticated HTTP API.
- 📥 Forward incoming SMS messages to a configurable server URL.
- 🗂️ Keep a local message history and manually retry failed forwarding attempts.
- ⚙️ Configure the server port and incoming-message destination.
- 🔑 Copy or regenerate the device's API token.
- 🔔 Run a foreground service with a persistent notification, remaining-time counter, and configurable reminders.
- 🎬 Extend service time by watching rewarded ads.

## 🎬 Demo

### Send an SMS from the server
An HTTP request triggers an SMS sent from the Android phone.



### Forward an incoming SMS to the server
An incoming SMS triggers a POST request to the configured endpoint.



## 🧰 Built with

- Kotlin and AndroidX
- Material Components, XML layouts, and View Binding
- MVVM with LiveData and Kotlin Coroutines
- Koin for dependency injection
- Ktor with Netty and Gson for the on-device HTTP API
- Retrofit and OkHttp for forwarding incoming messages
- Room for message history, with SharedPreferences for settings and service state
- Appodeal for banner and rewarded ads

## 🏗️ Project structure

The project is split into three Gradle modules:

| Module | Responsibility |
| --- | --- |
| `app` | Android UI, ViewModels, foreground service, HTTP API, SMS receiver, and dependency setup |
| `domain` | Models, repository contracts, and use cases for messages, settings, and service state |
| `data` | Room database, SharedPreferences, network requests, and repository implementations |

## 🚀 Build

### Requirements

- Android Studio with support for Android Gradle Plugin 8.2.0
- JDK 17
- Android SDK 34

Clone the repository and build the debug app with the included Gradle wrapper:

```bash
git clone https://github.com/PavkaBrika/sms_modem.git
cd sms_modem
bash gradlew :app:assembleDebug
```

You can also open the cloned directory in Android Studio, let Gradle sync, then run the `app` configuration on a device.

📱 **Minimum Android version:** Android 5.0 (API 21). Sending and receiving real SMS messages requires a phone with an active SIM and SMS service.

## 🔌 Getting started

1. Launch the app and grant the requested SMS and notification permissions.
2. Open **Settings** to configure the port (default: `5555`) and, optionally, the URL for forwarding incoming messages.
3. Turn on the service. The main screen and service notification show the device's IP address and port.
4. Open **How to use?** to copy the API token.
5. Send requests from a client that can reach the phone's IP address and port, for example on the same Wi-Fi network.

The service starts with 10 minutes of trial time. Rewarded ads add more time; the service stops when its remaining time runs out.

### Send an SMS

Replace the example IP address, token, and recipient number with your own values:

```bash
curl -X POST 'http://192.168.1.100:5555/api/sms' \
  -H 'Authorization: Bearer YOUR_API_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{"phoneNumber":"+15551234567","message":"Hello from SMS Modem"}'
```

The API returns HTTP `202` with:

```json
{"status":"accepted"}
```

This means the message was handed to Android's SMS manager; it does not confirm delivery to the recipient. Long messages are split into SMS parts.

| Field | Accepted value |
| --- | --- |
| `phoneNumber` | 3–20 digits, optionally prefixed with `+` |
| `message` | Nonblank text, up to 1,000 characters |

Missing or invalid credentials return HTTP `401`. Invalid JSON or field values return HTTP `400`, with a JSON `status` describing the error. Regenerating the token invalidates the previous one, so update all API clients afterward.

To check whether the HTTP server is running, request `GET /`; it returns `SERVICE ENABLED`.

The on-device server uses HTTP without encryption. Use it on a trusted network, since requests contain the API token and message text.

### Forward incoming SMS messages

Set the destination URL before starting the service. Restart the service after enabling forwarding or changing the port.

For each incoming SMS, the app makes a **POST** request to the configured URL with two URL-encoded query parameters:

| Parameter | Value |
| --- | --- |
| `cellNumber` | Sender's phone number |
| `text` | Received message text |

For example, a destination of `https://example.com/sms` receives a request shaped like:

```text
POST https://example.com/sms?cellNumber=%2B15551234567&text=Hello
```

These values are sent as query parameters. Configure your server to read them from the URL. Failed forwarding attempts remain in the message history and can be retried by tapping the failed entry.

## 🌍 Languages

The app includes English, Russian, German, and French resource translations.
