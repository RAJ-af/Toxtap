package com.himanshu.toxtap.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.himanshu.toxtap.model.ScannedSetting

object SettingsScanner {

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
        }.sortedBy { it.label }
    }
}
