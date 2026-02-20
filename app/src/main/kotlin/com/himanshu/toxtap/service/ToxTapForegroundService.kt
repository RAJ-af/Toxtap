package com.himanshu.toxtap.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.himanshu.toxtap.MainActivity
import com.himanshu.toxtap.R
import com.himanshu.toxtap.data.PreferenceManager
import com.himanshu.toxtap.util.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ToxTapForegroundService : Service() {

    private val CHANNEL_ID = "toxtap_service_channel"
    private val NOTIFICATION_ID = 1

    private lateinit var overlayManager: GestureOverlayManager
    private lateinit var preferenceManager: PreferenceManager
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        overlayManager = GestureOverlayManager(this)
        preferenceManager = PreferenceManager(this)

        observeOverlayPreference()
    }

    private fun observeOverlayPreference() {
        preferenceManager.isOverlayEnabled
            .onEach { enabled ->
                if (enabled && PermissionManager.hasOverlayPermission(this)) {
                    overlayManager.showOverlay()
                } else {
                    overlayManager.hideOverlay()
                }
            }
            .launchIn(serviceScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        overlayManager.hideOverlay()
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
