package com.example.domain.chat.model

import com.example.domain.message.model.Message
import com.example.domain.userdata.model.UserData

data class Chat(
    val id: String = "",
    val users: List<UserData> = emptyList(),
    val messages: List<Message> = emptyList()
)
