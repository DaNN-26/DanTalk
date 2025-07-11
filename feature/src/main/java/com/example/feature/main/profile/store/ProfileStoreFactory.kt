package com.example.feature.main.profile.store

import android.util.Patterns
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.data.user.api.model.UserData
import com.example.feature.main.profile.store.ProfileStore.Intent
import com.example.feature.main.profile.store.ProfileStore.Label
import com.example.feature.main.profile.store.ProfileStore.State
import com.example.feature.main.profile.util.ProfileValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileStoreFactory(
    private val factory: StoreFactory,
    private val userRepository: UserRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
) {
    sealed interface Action {
        class SetUser(val user: UserData) : Action
    }

    sealed interface Msg {
        class SetUser(val userData: UserData) : Msg
        class UpdateNewUserData(val newUserData: UserData) : Msg
        class UpdateValidation(val validation: ProfileValidation) : Msg
    }

    fun create(): ProfileStore =
        object : ProfileStore,
            Store<Intent, State, Label> by factory.create<Intent, Action, Msg, State, Label>(
                name = "ProfileStore",
                initialState = State(),
                bootstrapper = coroutineBootstrapper {
                    launch(Dispatchers.IO) {
                        userDataStoreRepository.getUserData.collect { user ->
                            withContext(Dispatchers.Main) {
                                dispatch(Action.SetUser(user))
                            }
                        }
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<Action.SetUser> {
                        dispatch(Msg.SetUser(it.user))
                    }
                    onIntent<Intent.UpdateNewUserData> { dispatch(Msg.UpdateNewUserData(it.newUserData)) }
                    onIntent<Intent.SaveNewUserData> {
                        if (state().currentUser != null)
                            launch {
                                validateInput(state().newUserData, state().currentUser!!)
                                    .let { dispatch(Msg.UpdateValidation(it)) }
                                if (state().validation !is ProfileValidation.Valid) return@launch
                                val newUserData = state().newUserData.copy(
                                    id = state().currentUser!!.id
                                )
                                saveNewUserData(newUserData)
                            }
                    }
                    onIntent<Intent.NavigateBack> { publish(Label.NavigateBack) }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.SetUser -> copy(
                            currentUser = msg.userData,
                            newUserData = msg.userData
                        )
                        is Msg.UpdateNewUserData -> copy(newUserData = msg.newUserData)
                        is Msg.UpdateValidation -> copy(validation = msg.validation)
                    }
                }
            ) {}

    private suspend fun validateInput(
        newUserData: UserData,
        currentUser: UserData,
    ): ProfileValidation = withContext(Dispatchers.IO) {
        when {
            newUserData.email.isEmpty() -> ProfileValidation.EmptyEmail
            newUserData.username.isEmpty() -> ProfileValidation.EmptyUsername
            newUserData.firstname.isEmpty() -> ProfileValidation.EmptyFirstname

            !Patterns.EMAIL_ADDRESS.matcher(
                newUserData.email
            ).matches() -> ProfileValidation.InvalidEmailFormat

            newUserData.username != currentUser.username &&
                    userRepository.isValueExists(
                        field = "username",
                        value = newUserData.username
                    ) -> ProfileValidation.UsernameExists

            newUserData.email != currentUser.email &&
                    userRepository.isValueExists(
                        field = "email",
                        value = newUserData.email
                    ) -> ProfileValidation.EmailExists

            else -> ProfileValidation.Valid
        }
    }

    private suspend fun saveNewUserData(newUserData: UserData) = withContext(Dispatchers.IO) {
        userRepository.updateUser(newUserData)
        userDataStoreRepository.saveUserData(newUserData)
    }
}