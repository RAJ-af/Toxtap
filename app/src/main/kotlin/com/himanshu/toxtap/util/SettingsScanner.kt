package com.himanshu.toxtap.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.himanshu.toxtap.model.ScannedSetting

object SettingsScanner {

    private val GESTURE_KEYWORDS = listOf("gesture", "tap", "wake", "motion", "double", "lift")

    fun scanSettings(context: Context): List<ScannedSetting> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setPackage("com.android.settings")

        val activities = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0L))
        } else {
            pm.queryIntentActivities(intent, 0)
        }

        return activities.filter { it.activityInfo.exported }.map { resolveInfo ->
            ScannedSetting(
                label = resolveInfo.loadLabel(pm).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                activityName = resolveInfo.activityInfo.name
            )
        }.sortedWith(compareByDescending<ScannedSetting> { isGestureRelated(it) }.thenBy { it.label })
    }

    private fun isGestureRelated(setting: ScannedSetting): Boolean {
        val label = setting.label.lowercase()
        val activity = setting.activityName.lowercase()
        return GESTURE_KEYWORDS.any { label.contains(it) || activity.contains(it) }
    }

    fun findNativeDoubleTapSetting(context: Context): ScannedSetting? {
        val settings = scanSettings(context)
        return settings.find {
            val label = it.label.lowercase()
            (label.contains("double tap") || label.contains("tap to wake")) && !it.activityName.contains("Demo")
        }
    }
}
