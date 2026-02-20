package com.himanshu.toxtap.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.os.Build
import android.util.Log

class ToxTapAccessibilityService : AccessibilityService() {

    companion object {
        private var instance: ToxTapAccessibilityService? = null

        fun performGlobalAction(action: Int): Boolean {
            val service = instance
            if (service == null) {
                Log.e("ToxTap", "Accessibility service not running, cannot perform action: $action")
                return false
            }
            return try {
                service.performGlobalAction(action)
            } catch (e: Exception) {
                Log.e("ToxTap", "Error performing global action: $action", e)
                false
            }
        }

        fun isServiceRunning(): Boolean = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not needed for this app's main features
    }

    override fun onInterrupt() {
    }

    override fun onUnbind(intent: android.content.Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }
}
