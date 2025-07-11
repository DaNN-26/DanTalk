package com.example.feature.main.people.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.data.user.api.model.UserData
import com.example.feature.main.people.store.PeopleStore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PeopleStoreFactory(
    private val factory: StoreFactory,
    private val userRepository: UserRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
) {
    sealed interface Msg {
        class OnQueryChange(val query: String) : Msg
        class UpdateUsers(val usersByQuery: List<UserData>) : Msg
        class UpdateLoading(val isLoading: Boolean) : Msg
    }

    fun create(): PeopleStore =
        object : PeopleStore,
            Store<Intent, State, Label> by factory.create<Intent, Nothing, Msg, State, Label>(
                name = "PeopleStore",
                initialState = State(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<Intent.OnQueryChange> {
                        dispatch(Msg.OnQueryChange(it.query))
                        dispatch(Msg.UpdateLoading(true))
                        launch {
                            val query = it.query.trim()
                            val users = getUsersByQuery(query)
                            dispatch(Msg.UpdateUsers(users.await()))
                            dispatch(Msg.UpdateLoading(false))
                        }
                    }
                    onIntent<Intent.NavigateBack> { publish(Label.NavigateBack) }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.OnQueryChange -> copy(query = msg.query)
                        is Msg.UpdateUsers -> copy(usersByQuery = msg.usersByQuery)
                        is Msg.UpdateLoading -> copy(isLoading = msg.isLoading)
                    }
                }
            ) {}

    private suspend fun getUsersByQuery(query: String) =
        coroutineScope {
            async(Dispatchers.IO) {
                val currentUser = userDataStoreRepository.getUserData.first()
                val users = userRepository.getUsersByQuery(query).toMutableList()
                users.remove(currentUser)
                return@async users
            }
        }
}