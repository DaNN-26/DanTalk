package com.example.mvi.main.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileState(
    val profileName: String = ""
)
