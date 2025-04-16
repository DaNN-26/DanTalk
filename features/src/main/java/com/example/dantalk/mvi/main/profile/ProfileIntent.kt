package com.example.dantalk.mvi.main.profile

sealed class ProfileIntent {
    data object InitializeProfile : com.example.dantalk.mvi.main.profile.ProfileIntent()
    data object NavigateBack : com.example.dantalk.mvi.main.profile.ProfileIntent()
}