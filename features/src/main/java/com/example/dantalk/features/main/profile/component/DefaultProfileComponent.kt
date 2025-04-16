package com.example.dantalk.features.main.profile.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.example.dantalk.mvi.main.profile.ProfileIntent
import com.example.dantalk.mvi.main.profile.ProfileState

class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val navigateBack: () -> Unit
) : ComponentContext by componentContext, ProfileComponent {

    private val _state = MutableValue(
        stateKeeper.consume(PROFILE_COMPONENT, ProfileState.serializer()) ?: ProfileState()
    )

    override val state = _state

    override fun processIntent(intent: ProfileIntent) {
        when(intent) {
            is ProfileIntent.InitializeProfile -> { /*TODO*/ }
            is ProfileIntent.NavigateBack -> navigateBack()
        }
    }

    companion object {
        const val PROFILE_COMPONENT = "PROFILE_COMPONENT"
    }
}