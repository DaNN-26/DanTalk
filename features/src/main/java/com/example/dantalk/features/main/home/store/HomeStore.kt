package com.example.dantalk.features.main.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.main.home.store.HomeStore.Intent
import com.example.dantalk.features.main.home.store.HomeStore.Label
import com.example.dantalk.features.main.home.store.HomeStore.State
import com.example.domain.chat.model.Chat
import com.example.domain.userdata.model.UserData

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