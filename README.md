# ToxTap

ToxTap is a gesture, shortcut, and accessibility utility app for Android. It allows users to perform system actions through gestures on a transparent overlay, create custom shortcuts, and scan for hidden system settings.

## Features

- **Gesture Control System**: Perform actions using Double Tap or Swipes (Up, Down, Left, Right) anywhere on the screen via a transparent overlay. Includes adjustable sensitivity control.
- **Accessibility Actions**: Quick access to Back, Home, Recents, Notifications, and Lock Screen.
- **Shortcut Maker**: Pin shortcuts to your home screen for accessibility actions or specific app screens.
- **Settings Scanner**: Discover and open activities from the system Settings app. Smart-detects and prioritizes gesture and motion-related settings.
- **Native Gesture Integration**: Detects if your device has native gesture support (like Double Tap to Wake) and recommends using it for optimal performance.
- **Foreground Stability**: Persistent background service ensures the gesture engine remains active.

## Permissions Required

- **Overlay Permission**: To show the transparent gesture detection layer.
- **Accessibility Service**: To perform system-level actions (Back, Home, etc.).
- **Battery Optimization Exemption**: To prevent the system from killing the background service.

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM with Jetpack Navigation
- **UI**: Material 3 with ViewBinding
- **Persistence**: Jetpack DataStore
- **Target SDK**: 34

## How to use

1.  Open the app and grant the necessary permissions (Overlay and Accessibility).
2.  Go to **Gesture Controls** to enable the overlay and map gestures to actions.
3.  Use **Shortcut Maker** to pin your favorite actions to the home screen.
4.  Use **Settings Scanner** to find specific system settings quickly.
5.  Check **Stability Settings** to ensure the app is not optimized by the battery manager.
