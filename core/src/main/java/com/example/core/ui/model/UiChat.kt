package com.example.core.ui.model

data class UiChat(
    val id: String,
    val user: UiUserData,
    val messages: List<UiMessage> = emptyList(),
    val unreadMessagesCount: Int = 0
)
