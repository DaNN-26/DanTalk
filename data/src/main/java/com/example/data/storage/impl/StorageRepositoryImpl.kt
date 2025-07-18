package com.example.data.storage.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.storage.api.StorageRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class StorageRepositoryImpl(
    private val client: SupabaseClient,
    private val context: Context
) : StorageRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun postImage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val bytes = inputStream.readBytes()
        val id = Uuid.random().toString()
        val bucket = client.storage.from("avatar")
        bucket.upload("$id.png", bytes) {
            upsert = false
        }.let { response ->
            val storagePath = "https://xkmiymytxnnljfpbyagk.supabase.co/storage/v1/object/public/avatar//"
            return storagePath + response.path
        }
    }
}