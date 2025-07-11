package com.example.data.chat.api.model

data class Message(
    val id: String = "",
    val sender: String = "",
    val message: String = "",
    val read: Boolean = false,
    val sentAt: Long = System.currentTimeMillis()
)