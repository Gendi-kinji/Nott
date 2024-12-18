package com.example.nott

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class MyApp : Application() {

    companion object {
        const val CHANNEL_ID = "Nott"
        const val CHANNEL_NAME = "Notifications"
        const val CHANNEL_DESCRIPTION = "Channel for periodic tasks"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val manager = getSystemService(NotificationManager::class.java)
            if (manager?.getNotificationChannel(CHANNEL_ID) == null) {
                manager?.createNotificationChannel(channel)
                Log.d("MyApp", "Notification channel created: $CHANNEL_NAME")
            } else {
                Log.d("MyApp", "Notification channel already exists: $CHANNEL_NAME")
            }
        }
    }
}
