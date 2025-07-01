package com.example.dantalk.features.main.chat.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.domain.model.chat.Chat
import com.example.dantalk.features.main.chat.store.ChatStore.*

interface ChatStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        /* TODO */
    }

    data class State(
        val chats: List<Chat> = emptyList()
    )
}