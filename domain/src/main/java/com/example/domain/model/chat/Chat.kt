package com.example.domain.model.chat

import com.example.domain.model.message.Message
import com.example.domain.model.userdata.UserData

data class Chat(
    val id: String = "",
    val users: List<UserData> = emptyList(),
    val messages: List<Message> = emptyList()
)
