package com.himanshu.toxtap.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.himanshu.toxtap.MainActivity
import com.himanshu.toxtap.R
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction

object ShortcutHelper {

    fun createPinnedShortcut(context: Context, gestureAction: GestureAction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)

            if (shortcutManager.isRequestPinShortcutSupported) {
                val intent = Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("action_type", gestureAction.type.name)
                    putExtra("action_data", gestureAction.data)
                }

                val pinShortcutInfo = ShortcutInfo.Builder(context, "shortcut_${System.currentTimeMillis()}")
                    .setShortLabel(gestureAction.label)
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .build()

                shortcutManager.requestPinShortcut(pinShortcutInfo, null)
            }
        }
    }

    fun createScreenShortcut(context: Context, label: String, screenId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)

            if (shortcutManager.isRequestPinShortcutSupported) {
                val intent = Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("screen_id", screenId)
                }

                val pinShortcutInfo = ShortcutInfo.Builder(context, "screen_${screenId}")
                    .setShortLabel(label)
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .build()

                shortcutManager.requestPinShortcut(pinShortcutInfo, null)
            }
        }
    }
}
