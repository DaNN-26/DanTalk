package com.example.data.storage.impl.entity

import kotlinx.serialization.Serializable

@Serializable
internal data class ImageEntity(
    val data: ImageData?,
    val success: Boolean,
    val status: Int
)

@Serializable
internal data class ImageData(
    val url: String
)
