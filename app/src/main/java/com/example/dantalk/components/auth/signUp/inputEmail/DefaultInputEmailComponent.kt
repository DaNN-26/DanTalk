package com.example.dantalk.components.auth.signUp.inputEmail

import android.util.Log
import android.util.Patterns
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.example.mvi.auth.signUp.inputEmail.InputEmailIntent
import com.example.mvi.auth.signUp.inputEmail.InputEmailState
import com.example.mvi.auth.signUp.inputEmail.InputEmailValidation
import com.example.network.auth.domain.model.User
import com.example.network.auth.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultInputEmailComponent(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val navigateToInputName: (User) -> Unit,
    private val navigateBack: () -> Unit
) : ComponentContext by componentContext, InputEmailComponent {

    private val _state = MutableValue(
        stateKeeper.consume(INPUT_EMAIL_COMPONENT, InputEmailState.serializer()) ?: InputEmailState()
    )

    override val state = _state

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun processIntent(intent: InputEmailIntent) {
        when(intent) {
            is InputEmailIntent.OnUsernameChange -> _state.update { it.copy(username = intent.username) }
            is InputEmailIntent.OnEmailChange -> _state.update { it.copy(email = intent.email) }
            is InputEmailIntent.NavigateToInputName -> navigateToNextScreen()
            is InputEmailIntent.NavigateBack -> navigateBack()
        }
    }

    private suspend fun validateUserData() {
        val validation = when {
            state.value.email.isBlank() && state.value.username.isBlank() -> InputEmailValidation.EmptyAllFields
            state.value.username.isBlank() -> InputEmailValidation.EmptyUsername
            state.value.email.isBlank() -> InputEmailValidation.EmptyEmail
            !Patterns.EMAIL_ADDRESS.matcher(state.value.email)
                .matches() -> InputEmailValidation.InvalidEmailFormat
            isUsernameExists() -> InputEmailValidation.UsernameAlreadyExist
            isEmailExists() -> InputEmailValidation.EmailAlreadyExist

            else -> InputEmailValidation.Valid
        }
        _state.update { it.copy(
            validation = validation,
            isLoading = false
        ) }
    }

    private suspend fun isUsernameExists() : Boolean =
        userRepository.isValueExists("username", state.value.username)

    private suspend fun isEmailExists() : Boolean =
        userRepository.isValueExists("email", state.value.email)

    private fun navigateToNextScreen() {
        scope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                validateUserData()
                withContext(Dispatchers.Main) {
                    if (state.value.validation is InputEmailValidation.Valid) {
                        val user = User(email = state.value.email, username = state.value.username)
                        navigateToInputName(user)
                    }
                }
            } catch (e: Exception) {
                Log.d("DefaultInputEmailComponent", "${e.message}")
                _state.update { it.copy(isLoading = false)}
            }
        }
    }

    companion object {
        const val INPUT_EMAIL_COMPONENT = "INPUT_EMAIL_COMPONENT"
    }
}