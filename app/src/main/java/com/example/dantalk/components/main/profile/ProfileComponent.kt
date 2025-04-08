package com.example.dantalk.components.main.profile

import com.arkivanov.decompose.value.Value
import com.example.mvi.main.profile.ProfileIntent
import com.example.mvi.main.profile.ProfileState

interface ProfileComponent {
    val state: Value<ProfileState>

    fun processIntent(intent: ProfileIntent)
}