package com.example.feature.auth.sign_in.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.auth.exception.SignInException
import com.example.data.auth.api.AuthRepository
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.feature.auth.sign_in.store.SignInStore.Intent
import com.example.feature.auth.sign_in.store.SignInStore.Label
import com.example.feature.auth.sign_in.store.SignInStore.State
import com.example.feature.auth.sign_in.util.SignInValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInStoreFactory(
    private val factory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
) {
    private sealed interface Msg {
        class OnEmailChange(val email: String) : Msg
        class OnPasswordChange(val password: String) : Msg
        class UpdateValidation(val validation: SignInValidation) : Msg
        class UpdateLoading(
            val isLoading: Boolean = false,
            val isSuccessful: Boolean = false,
        ) : Msg
    }

    fun create(): SignInStore =
        object : SignInStore,
            Store<Intent, State, Label> by factory.create<Intent, Nothing, Msg, State, Label>(
                name = "SignInStore",
                initialState = State(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<Intent.OnEmailChange> { dispatch(Msg.OnEmailChange(it.email)) }
                    onIntent<Intent.OnPasswordChange> { dispatch(Msg.OnPasswordChange(it.password)) }
                    onIntent<Intent.SignIn> {
                        validateInput(state().email, state().password)
                            .let { dispatch(Msg.UpdateValidation(it)) }
                        if (state().validation !is SignInValidation.Valid) return@onIntent
                        dispatch(Msg.UpdateLoading(true))
                        launch { signIn() }
                    }
                    onIntent<Intent.NavigateToSignUp> { publish(Label.NavigateToSignUp) }
                    onIntent<Intent.DismissDialog> {
                        dispatch(Msg.UpdateLoading(isSuccessful = false))
                        publish(Label.NavigateToHome)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.OnEmailChange -> copy(email = msg.email)
                        is Msg.OnPasswordChange -> copy(password = msg.password)
                        is Msg.UpdateValidation -> copy(validation = msg.validation)
                        is Msg.UpdateLoading -> copy(
                            isLoading = msg.isLoading,
                            isSuccessful = msg.isSuccessful
                        )
                    }
                }
            ) {}

    private fun validateInput(email: String, password: String): SignInValidation =
        when {
            email.isEmpty() && password.isEmpty() -> SignInValidation.EmptyAllFields
            email.isEmpty() -> SignInValidation.EmptyEmail
            password.isEmpty() -> SignInValidation.EmptyPassword
            else -> SignInValidation.Valid
        }

    private suspend fun CoroutineExecutorScope<State, Msg, Nothing, Nothing>.signIn() {
        withContext(Dispatchers.IO) {
            authRepository.login(state().email, state().password)
        }.onSuccess { uid ->
            saveUserData(uid)
            dispatch(Msg.UpdateLoading(isSuccessful = true))
        }.onFailure {
            val validation = when (it) {
                is SignInException.NetworkError -> SignInValidation.NetworkError
                is SignInException.InvalidEmailFormat -> SignInValidation.InvalidEmailFormat
                is SignInException.InvalidCredentials -> SignInValidation.InvalidCredentials
                else -> SignInValidation.NetworkError
            }
            dispatch(Msg.UpdateLoading(isLoading = false))
            dispatch(Msg.UpdateValidation(validation))
        }
    }

    private suspend fun saveUserData(userId: String) = withContext(Dispatchers.IO) {
        val userData = userRepository.getUser(userId)
        userDataStoreRepository.saveUserData(userData)
    }
}