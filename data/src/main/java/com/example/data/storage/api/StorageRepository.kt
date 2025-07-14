package com.example.data.storage.api

import android.net.Uri
import com.example.data.storage.api.model.Image

interface StorageRepository {
    suspend fun postImage(uri: Uri): Image
}