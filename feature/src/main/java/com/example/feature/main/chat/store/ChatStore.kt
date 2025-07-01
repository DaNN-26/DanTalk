package com.example.feature.main.chat.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.feature.main.chat.store.ChatStore.*
import com.example.data.chat.api.model.Chat

interface ChatStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        /* TODO */
    }

    data class State(
        val chats: List<Chat> = emptyList()
    )
}