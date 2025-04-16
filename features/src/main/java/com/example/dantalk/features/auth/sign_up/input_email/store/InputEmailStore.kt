package com.example.dantalk.features.auth.sign_up.input_email.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.auth.sign_up.input_email.store.InputEmailStore.Intent
import com.example.dantalk.features.auth.sign_up.input_email.store.InputEmailStore.Label
import com.example.dantalk.features.auth.sign_up.input_email.store.InputEmailStore.State
import com.example.dantalk.features.auth.sign_up.input_email.util.InputEmailValidation
import com.example.domain.userdata.model.UserData

interface InputEmailStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class OnEmailChange(val email: String) : Intent
        class OnUsernameChange(val username: String) : Intent
        data object NavigateNext : Intent
        data object NavigateBack : Intent
    }

    data class State(
        val username: String = "",
        val email: String = "",
        val validation: InputEmailValidation = InputEmailValidation.Valid,
        val isLoading: Boolean = false
    )

    sealed interface Label {
        class NavigateNext(val userData: UserData) : Label
        data object NavigateToSignIn : Label
    }
}