package com.example.dantalk.features.auth.sign_up.input_password.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.Intent
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.Label
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.State
import com.example.dantalk.features.auth.sign_up.input_password.util.InputPasswordValidation
import com.example.domain.userdata.model.UserData
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InputPasswordStoreFactory(
    private val factory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val currentUserData: UserData,
) {
    private sealed interface Msg {
        class OnPasswordChange(val password: String) : Msg
        class OnRepeatablePasswordChange(val repeatablePassword: String) : Msg
        class UpdateValidation(val validation: InputPasswordValidation) : Msg
        class UpdateLoading(
            val isLoading: Boolean = false,
            val isSuccessful: Boolean = false,
        ) : Msg
    }

    fun create(): InputPasswordStore =
        object : InputPasswordStore,
            Store<Intent, State, Label> by factory.create<Intent, Nothing, Msg, State, Label>(
                name = "InputPasswordStore",
                initialState = State(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<Intent.OnPasswordChange> { dispatch(Msg.OnPasswordChange(it.password)) }
                    onIntent<Intent.OnRepeatablePasswordChange> { dispatch(Msg.OnRepeatablePasswordChange(it.repeatablePassword)) }
                    onIntent<Intent.SignUp> {
                        validatePassword(state().password, state().repeatablePassword)
                            .let { dispatch(Msg.UpdateValidation(it)) }
                        if (state().validation != InputPasswordValidation.Valid) return@onIntent
                        dispatch(Msg.UpdateLoading(true))
                        launch { signUp() }
                    }
                    onIntent<Intent.DismissDialog> {
                        dispatch(Msg.UpdateLoading(isSuccessful = false))
                        publish(Label.NavigateToHome)
                    }
                    onIntent<Intent.NavigateBack> { publish(Label.NavigateBack) }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.OnPasswordChange -> copy(password = msg.password)
                        is Msg.OnRepeatablePasswordChange -> copy(repeatablePassword = msg.repeatablePassword)
                        is Msg.UpdateValidation -> copy(validation = msg.validation)
                        is Msg.UpdateLoading -> copy(
                            isLoading = msg.isLoading,
                            isSuccessful = msg.isSuccessful
                        )
                    }
                }
            ) {}

    private fun validatePassword(
        password: String,
        repeatablePassword: String,
    ): InputPasswordValidation =
        when {
            password.isBlank() -> InputPasswordValidation.EmptyPassword
            password != repeatablePassword -> InputPasswordValidation.NotMatchesPasswords
            else -> InputPasswordValidation.Valid
        }

    private suspend fun CoroutineExecutorScope<State, Msg, Nothing, Nothing>.signUp() {
        withContext(Dispatchers.IO) {
            authRepository.createUser(currentUserData.email, state().password)
        }.onSuccess { user ->
            saveUserData(user.uid)
            dispatch(Msg.UpdateLoading(isSuccessful = true))
        }.onFailure {
            val validation = when (it) {
                is FirebaseAuthWeakPasswordException -> InputPasswordValidation.PasswordIsTooShort
                is FirebaseNetworkException -> InputPasswordValidation.NetworkError
                else -> InputPasswordValidation.NetworkError
            }
            dispatch(Msg.UpdateLoading(isLoading = false))
            dispatch(Msg.UpdateValidation(validation))
        }
    }

    private suspend fun saveUserData(userId: String) = withContext(Dispatchers.IO) {
        val userData = currentUserData.copy(userId = userId)
        userRepository.createUser(userData)
        userDataStore.saveUserData(userData)
    }
}