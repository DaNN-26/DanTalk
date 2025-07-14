package com.example.data.storage.impl.mapper

import com.example.data.storage.api.model.Image
import com.example.data.storage.impl.entity.ImageEntity

internal fun ImageEntity.toImage() =
    Image(
        url = data?.url ?: ""
    )