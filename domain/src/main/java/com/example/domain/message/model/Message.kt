package com.example.domain.message.model

data class Message(
    val id: String,
    val sender: String,
    val message: String,
    val delivered: Boolean = false,
    val read: Boolean = false,
    val sentAt: Long = System.currentTimeMillis()
)
