package com.example.core.ui.model

data class UiMessage(
    val id: String,
    val isCurrentUserMessage: Boolean,
    val message: String,
    val read: Boolean,
    val isPending: Boolean,
    val isPhoto: Boolean,
    val date: String,
    val time: String
)
