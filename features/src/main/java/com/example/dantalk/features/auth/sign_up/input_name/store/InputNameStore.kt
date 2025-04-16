package com.example.dantalk.features.auth.sign_up.input_name.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.auth.sign_up.input_name.store.InputNameStore.*
import com.example.domain.userdata.model.UserData

interface InputNameStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class OnFirstnameChange(val firstname: String) : Intent
        class OnLastnameChange(val lastname: String) : Intent
        class OnPatronymicChange(val patronymic: String) : Intent
        data object NavigateNext : Intent
        data object NavigateBack : Intent
    }

    data class State(
        val firstname: String = "",
        val lastname: String = "",
        val patronymic: String = "",
        val isEmptyFirstname: Boolean = false
    )

    sealed interface Label {
        class NavigateNext(val userData: UserData) : Label
        data object NavigateBack : Label
    }
}