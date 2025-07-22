package com.example.data.storage.api

import android.net.Uri

interface StorageRepository {
    suspend fun postImage(uri: Uri): String
    suspend fun downloadImage(url: String): ByteArray
}