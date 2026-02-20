package com.himanshu.toxtap.service

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.util.PermissionManager

object ActionExecutor {

    private var isFlashlightOn = false

    fun execute(context: Context, action: GestureAction) {
        try {
            when (action.type) {
                ActionType.BACK -> performAccessibilityAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK, context)
                ActionType.HOME -> performAccessibilityAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME, context)
                ActionType.RECENTS -> performAccessibilityAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS, context)
                ActionType.NOTIFICATIONS -> performAccessibilityAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS, context)
                ActionType.LOCK_SCREEN -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (!performAccessibilityAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN, context)) {
                            Log.e("ToxTap", "Failed to perform Lock Screen action")
                        }
                    } else {
                        Toast.makeText(context, "Lock Screen requires Android P+", Toast.LENGTH_SHORT).show()
                    }
                }
                ActionType.LAUNCH_APP -> {
                    action.data?.let { packageName ->
                        val pm = context.packageManager
                        val intent = pm?.getLaunchIntentForPackage(packageName)
                        if (intent != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                ActionType.TOGGLE_FLASHLIGHT -> toggleFlashlight(context)
                ActionType.OPEN_SETTINGS_SCREEN -> {
                    action.data?.let { activityName ->
                        val intent = Intent().apply {
                            setClassName("com.android.settings", activityName)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                }
                ActionType.NONE -> {}
            }
        } catch (e: Exception) {
            Log.e("ToxTap", "Action execution error", e)
            Toast.makeText(context, "Action failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performAccessibilityAction(actionId: Int, context: Context): Boolean {
        if (!ToxTapAccessibilityService.isServiceRunning()) {
            Toast.makeText(context, "Please enable ToxTap Accessibility Service", Toast.LENGTH_SHORT).show()
            PermissionManager.requestAccessibilityPermission(context)
            return false
        }
        return ToxTapAccessibilityService.performGlobalAction(actionId)
    }

    private fun toggleFlashlight(context: Context) {
        if (!context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(context, "No flashlight available", Toast.LENGTH_SHORT).show()
            return
        }
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList.getOrNull(0)
            if (cameraId != null) {
                isFlashlightOn = !isFlashlightOn
                cameraManager.setTorchMode(cameraId, isFlashlightOn)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Flashlight error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
