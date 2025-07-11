package com.example.data.chat.impl.mapper

import com.example.data.chat.api.model.Chat
import com.example.data.chat.impl.entity.ChatEntity
import com.example.data.user.api.model.UserData

internal fun ChatEntity.toDomain(
    users: List<UserData>
) =
    Chat(
        id = id,
        users = users
    )