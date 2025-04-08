package com.example.dantalk.components.auth.signUp.inputName

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.mvi.auth.signUp.inputName.InputNameIntent
import com.example.mvi.auth.signUp.inputName.InputNameState
import com.example.network.auth.domain.model.User

class DefaultInputNameComponent(
    componentContext: ComponentContext,
    private val navigateToInputPassword: (User) -> Unit,
    private val navigateBack: () -> Unit,
    private val currentUserData: User
) : ComponentContext by componentContext, InputNameComponent {

    private val _state = MutableValue(
        stateKeeper.consume(INPUT_NAME_COMPONENT, InputNameState.serializer()) ?: InputNameState()
    )

    override val state = _state

    override fun processIntent(intent: InputNameIntent) {
        when(intent) {
            is InputNameIntent.OnFirstnameChange -> _state.update { it.copy(firstname = intent.firstname) }
            is InputNameIntent.OnLastnameChange -> _state.update { it.copy(lastname = intent.lastname) }
            is InputNameIntent.OnPatronymicChange -> _state.update { it.copy(patronymic = intent.patronymic) }
            is InputNameIntent.NavigateToInputPassword -> navigateToNextScreen()
            is InputNameIntent.NavigateBack -> navigateBack()
        }
    }

    private fun navigateToNextScreen() {
        if(state.value.firstname.isBlank())
            _state.update { it.copy(isEmptyFirstname = true) }
        else {
            _state.update { it.copy(isEmptyFirstname = false) }
            val user = currentUserData.copy(
                firstname = state.value.firstname,
                lastname = state.value.lastname,
                patronymic = state.value.patronymic
            )
            navigateToInputPassword(user)
        }
    }

    companion object {
        const val INPUT_NAME_COMPONENT = "INPUT_NAME_COMPONENT"
    }
}