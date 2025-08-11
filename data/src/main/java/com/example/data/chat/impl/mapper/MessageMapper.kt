package com.example.data.chat.impl.mapper

import com.example.data.chat.api.model.Message
import com.example.data.chat.impl.entity.MessageEntity

internal fun Message.toEntity() =
    MessageEntity(
        sender = sender,
        message = message,
        read = read,
        pending = isPending,
        photo = isPhoto,
        sentAt = sentAt
    )

internal fun MessageEntity.toDomain(id: String) =
    Message(
        id = id,
        sender = sender,
        message = message,
        read = read,
        isPending = pending,
        isPhoto = photo,
        sentAt = sentAt
    )