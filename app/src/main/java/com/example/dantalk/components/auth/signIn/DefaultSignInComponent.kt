package com.example.dantalk.components.auth.signIn

import android.util.Log
import android.util.Patterns
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.example.mvi.auth.signIn.SignInIntent
import com.example.mvi.auth.signIn.SignInState
import com.example.mvi.auth.signIn.SignInValidation
import com.example.mvi.auth.signUp.inputPassword.InputPasswordValidation
import com.example.network.auth.domain.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DefaultSignInComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val navigateToSignUp: () -> Unit,
    private val navigateToHome: () -> Unit
) : ComponentContext by componentContext, SignInComponent {

    private val _state = MutableValue(
        stateKeeper.consume(SIGN_IN_COMPONENT, SignInState.serializer()) ?: SignInState()
    )

    override val state = _state

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun processIntent(intent: SignInIntent) {
        when(intent) {
            is SignInIntent.OnEmailChange -> _state.update { it.copy(email = intent.email) }
            is SignInIntent.OnPasswordChange -> _state.update { it.copy(password = intent.password) }
            is SignInIntent.SignIn -> login()
            is SignInIntent.NavigateToHome -> {
                _state.update { it.copy(isSuccessful = false) }
                navigateToHome()
            }
            is SignInIntent.NavigateToSignUp -> navigateToSignUp()
        }
    }

    private fun validateUserData() {
        val validation = when {
            state.value.email.isBlank() && state.value.password.isBlank() -> SignInValidation.EmptyAllFields
            state.value.email.isBlank() -> SignInValidation.EmptyEmail
            state.value.password.isBlank() -> SignInValidation.EmptyPassword
            !Patterns.EMAIL_ADDRESS.matcher(state.value.email)
                .matches() -> SignInValidation.InvalidEmailFormat

            else -> SignInValidation.Valid
        }

        _state.update { it.copy(validation = validation) }
    }

    private fun login() {
        scope.launch {
            validateUserData()
            if(state.value.validation != SignInValidation.Valid) return@launch
            try {
                _state.update { it.copy(isLoading = true) }
                authRepository.login(
                    email = state.value.email,
                    password = state.value.password
                )
                _state.update { it.copy(
                    isSuccessful = true,
                    isLoading = false
                ) }
            } catch (e: FirebaseNetworkException) {
                Log.d("LoginUser", e.message.toString())
                _state.update { it.copy(
                    validation = SignInValidation.NetworkError,
                    isLoading = false
                ) }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Log.d("LoginUser", e.message.toString())
                _state.update { it.copy(
                    validation = SignInValidation.InvalidCredentials,
                    isLoading = false
                ) }
            } catch (e: FirebaseException) {
                Log.d("LoginUser", e.message.toString())
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    companion object {
        const val SIGN_IN_COMPONENT = "SIGN_IN_COMPONENT"
    }
}