package com.nvc.orderfood

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OrderApplication : Application() {

    companion object {
        const val CHANNEL_NOTIFICATION_FOREGROUND_ID = "CHANNEL_NOTIFICATION_ID"
        const val CHANNEL_NOTIFICATION_USER_ID = "CHANNEL_NOTIFICATION_USER_ID"
    }

    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelForeground = NotificationChannel(
                CHANNEL_NOTIFICATION_FOREGROUND_ID,
                "Notification",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            val channelUserNotification = NotificationChannel(
                CHANNEL_NOTIFICATION_USER_ID,
                "Notification User",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager?.createNotificationChannel(channelForeground)
            manager?.createNotificationChannel(channelUserNotification)
        }
    }

}