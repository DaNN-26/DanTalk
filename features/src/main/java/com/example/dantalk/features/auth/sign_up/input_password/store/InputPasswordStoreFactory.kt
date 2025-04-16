package com.example.dantalk.features.auth.sign_up.input_password.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.Intent
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.Label
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore.State
import com.example.dantalk.features.auth.sign_up.input_password.util.InputPasswordValidation


class InputPasswordStoreFactory(
    private val factory: StoreFactory,
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
        object : InputPasswordStore, Store<Intent, State, Label> by factory.create<Intent, Nothing, Msg, State, Label>(
            name = "InputPasswordStore",
            initialState = State(),
            executorFactory = coroutineExecutorFactory {
                onIntent<Intent.OnPasswordChange> { dispatch(Msg.OnPasswordChange(it.password)) }
                onIntent<Intent.OnRepeatablePasswordChange> { dispatch(Msg.OnRepeatablePasswordChange(it.repeatablePassword)) }
                onIntent<Intent.SignUp> {

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
                    is Msg.UpdateLoading -> copy(isLoading = msg.isLoading, isSuccessful = msg.isSuccessful)
                }
            }
        ) {}

    private suspend inline fun CoroutineExecutorScope<State, Msg, Nothing, Nothing>.signUp() {

    }
}