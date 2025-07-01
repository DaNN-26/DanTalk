package com.example.dantalk.features.main.search.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.search.store.SearchStore.Intent
import com.example.dantalk.features.main.search.store.SearchStore.Label
import com.example.dantalk.features.main.search.store.SearchStore.State
import com.example.domain.model.userdata.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchStoreFactory(
    private val factory: StoreFactory,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
) {
    sealed interface Msg {
        class OnQueryChange(val query: String) : Msg
        class UpdateUsers(val usersByQuery: List<UserData>) : Msg
        class UpdateLoading(val isLoading: Boolean) : Msg
    }

    fun create(): SearchStore =
        object : SearchStore,
            Store<Intent, State, Label> by factory.create<Intent, Nothing, Msg, State, Label>(
                name = "SearchStore",
                initialState = State(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<Intent.OnQueryChange> {
                        dispatch(Msg.OnQueryChange(it.query))
                        launch {
                            val query = it.query.trim()
                            if (query.isEmpty()) {
                                dispatch(Msg.UpdateUsers(emptyList()))
                                return@launch
                            }
                            if (!state().isLoading)
                                dispatch(Msg.UpdateLoading(true))
                            val users = getUsersByQuery(query)
                            dispatch(Msg.UpdateUsers(users.await()))
                            if (users.isCompleted)
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
                val currentUser = userDataStore.getUserData.first()
                val users = userRepository.getUsersByQuery(query).toMutableList()
                users.remove(currentUser)
                return@async users
            }
        }
}