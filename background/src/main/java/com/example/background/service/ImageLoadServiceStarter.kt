package com.example.background.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

object ImageLoadServiceStarter {
    fun post(context: Context, uri: Uri) {
        val intent = Intent(context, ImageLoadService::class.java).apply {
            putExtra("action", "POST")
            putExtra("uri", uri)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun download(context: Context, url: String) {
        val intent = Intent(context, ImageLoadService::class.java).apply {
            putExtra("action", "DOWNLOAD")
            putExtra("url", url)
        }
        ContextCompat.startForegroundService(context, intent)
    }
}