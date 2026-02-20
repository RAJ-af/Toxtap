package com.himanshu.toxtap.model

enum class ActionType {
    NONE,
    BACK,
    HOME,
    RECENTS,
    NOTIFICATIONS,
    LOCK_SCREEN,
    LAUNCH_APP,
    TOGGLE_FLASHLIGHT,
    OPEN_SETTINGS_SCREEN
}

data class GestureAction(
    val type: ActionType,
    val data: String? = null, // Package name for LAUNCH_APP, or Activity name for OPEN_SETTINGS_SCREEN
    val label: String
)

data class ScannedSetting(
    val label: String,
    val packageName: String,
    val activityName: String
)

enum class GestureType {
    DOUBLE_TAP,
    SWIPE_UP,
    SWIPE_DOWN,
    SWIPE_LEFT,
    SWIPE_RIGHT
}
