package com.example.data.storage.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.storage.api.StorageRepository
import com.example.data.storage.api.model.Image
import com.example.data.storage.impl.entity.ImageEntity
import com.example.data.storage.impl.mapper.toImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class StorageRepositoryImpl(
    private val client: HttpClient,
    private val context: Context
) : StorageRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun postImage(uri: Uri): Image {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val bytes = inputStream.readBytes()
        val id = Uuid.random().toString()
        val response = client.post("https://api.imgbb.com/1/upload") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("key", "768193e6f6cf1b7ee9a13f487439ba7a")
                        append("image", bytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$id.jpg\"")
                        })
                    }
                )
            )
        }
        return response.body<ImageEntity>().toImage()
    }
}