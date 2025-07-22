package com.example.background.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.background.R

const val IMAGE_LOAD_CHANNEL_ID = "image_load_channel"

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            IMAGE_LOAD_CHANNEL_ID,
            "Image Load",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Уведомление для загрузки изображений"
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

fun createNotification(context: Context, contentText: String, isOngoing: Boolean = true): Notification {
    createNotificationChannel(context)

    return NotificationCompat.Builder(context, IMAGE_LOAD_CHANNEL_ID)
        .setContentTitle("Работа с изображением")
        .setContentText(contentText)
        .setSmallIcon(R.drawable.download)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(isOngoing)
        .build()
}

fun showSuccessNotification(context: Context, text: String) {
    val notification = createNotification(context, text, false)
    val notifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notifyManager.notify(2, notification)
}