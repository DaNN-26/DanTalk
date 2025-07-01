package com.example.dantalk.features.main.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.main.profile.store.ProfileStore.Intent
import com.example.dantalk.features.main.profile.store.ProfileStore.Label
import com.example.dantalk.features.main.profile.store.ProfileStore.State
import com.example.dantalk.features.main.profile.util.ProfileValidation
import com.example.domain.model.userdata.UserData

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