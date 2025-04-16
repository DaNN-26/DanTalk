package com.example.dantalk.features.main.profile.component

import com.arkivanov.decompose.value.Value
import com.example.dantalk.mvi.main.profile.ProfileIntent
import com.example.dantalk.mvi.main.profile.ProfileState

interface ProfileComponent {
    val state: Value<ProfileState>

    fun processIntent(intent: ProfileIntent)
}