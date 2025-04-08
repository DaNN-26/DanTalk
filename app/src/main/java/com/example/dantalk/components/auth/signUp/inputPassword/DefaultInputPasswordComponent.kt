package com.example.dantalk.components.auth.signUp.inputPassword

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.mvi.auth.signUp.inputPassword.InputPasswordIntent
import com.example.mvi.auth.signUp.inputPassword.InputPasswordState
import com.example.mvi.auth.signUp.inputPassword.InputPasswordValidation
import com.example.network.auth.domain.model.User
import com.example.network.auth.domain.repository.AuthRepository
import com.example.network.auth.domain.repository.UserRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultInputPasswordComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigateToHome: () -> Unit,
    private val navigateBack: () -> Unit,
    private val currentUserData: User
) : ComponentContext by componentContext, InputPasswordComponent {

    private val _state = MutableValue(
        stateKeeper.consume(INPUT_PASSWORD_COMPONENT, InputPasswordState.serializer()) ?: InputPasswordState()
    )

    override val state = _state

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun processIntent(intent: InputPasswordIntent) {
        when(intent) {
            is InputPasswordIntent.OnPasswordChange -> _state.update { it.copy(password = intent.password) }
            is InputPasswordIntent.OnRepeatablePasswordChange -> _state.update { it.copy(repeatablePassword = intent.repeatablePassword) }
            is InputPasswordIntent.SignUp -> createUser(currentUserData)
            is InputPasswordIntent.NavigateToHome ->  {
                _state.update { it.copy(isSuccessful = false) }
                navigateToHome()
            }
            is InputPasswordIntent.NavigateBack -> navigateBack()
        }
    }

    private suspend fun checkPassword() = withContext(Dispatchers.Main) {
        val validation = when {
            state.value.password.isBlank() ->
                InputPasswordValidation.EmptyPassword
            state.value.password != state.value.repeatablePassword ->
                InputPasswordValidation.NotMatchesPasswords

            else -> InputPasswordValidation.Valid
        }
        _state.update { it.copy(validation = validation) }
    }

    private fun createUser(user: User) {
        scope.launch {
            checkPassword()
            if(state.value.validation != InputPasswordValidation.Valid) return@launch
            try {
                _state.update { it.copy(isLoading = true) }
                authRepository.createUser(
                    email = user.email,
                    password = state.value.password
                )
                userRepository.createUser(user)
                _state.update { it.copy(
                    isLoading = false,
                    isSuccessful = true
                ) }
            } catch (e: FirebaseAuthWeakPasswordException) {
                Log.d("CreateUser", e.toString())
                _state.update { it.copy(
                    validation = InputPasswordValidation.PasswordIsTooShort,
                    isLoading = false
                ) }
            } catch (e: FirebaseException) {
                Log.d("CreateUser", e.toString())
                _state.update { it.copy(
                    validation = InputPasswordValidation.NetworkError,
                    isLoading = false
                ) }
            }
        }
    }

    companion object {
        const val INPUT_PASSWORD_COMPONENT = "INPUT_PASSWORD_COMPONENT"
    }
}