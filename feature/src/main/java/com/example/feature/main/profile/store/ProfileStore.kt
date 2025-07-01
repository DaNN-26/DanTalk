package com.example.feature.main.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.feature.main.profile.store.ProfileStore.Intent
import com.example.feature.main.profile.store.ProfileStore.Label
import com.example.feature.main.profile.store.ProfileStore.State
import com.example.data.user.api.model.UserData
import com.example.feature.main.profile.util.ProfileValidation

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class UpdateNewUserData(val newUserData: UserData) : Intent
        data object SaveNewUserData : Intent
        data object NavigateBack : Intent
    }

    data class State(
        val currentUser: UserData? = null,
        val newUserData: UserData = UserData(),
        val validation: ProfileValidation = ProfileValidation.Valid
    )

    sealed interface Label {
        data object NavigateBack : Label
    }
}