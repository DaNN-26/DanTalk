package com.example.feature.main.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.data.chat.api.model.Chat
import com.example.data.user.api.model.UserData
import com.example.feature.main.home.store.HomeStore.Intent
import com.example.feature.main.home.store.HomeStore.Label
import com.example.feature.main.home.store.HomeStore.State

interface HomeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object NavigateToSearch : Intent
        data object NavigateToProfile : Intent
        data object NavigateToPeople : Intent
        data object SignOut : Intent
    }

    data class State(
        val chats: List<Chat> = emptyList(),
        val user: UserData = UserData(),
        val isLoading: Boolean = false
    )

    sealed interface Label {
        data object NavigateToSearch : Label
        data object NavigateToProfile : Label
        data object NavigateToPeople : Label
        data object NavigateToAuth : Label
    }
}