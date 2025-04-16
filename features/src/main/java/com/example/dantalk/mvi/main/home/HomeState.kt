package com.example.dantalk.mvi.main.home

import com.example.domain.userdata.model.UserData
import kotlinx.serialization.Serializable

@Serializable
data class HomeState(
    val chats: List<String> = emptyList(),
    val user: UserData = UserData()
)
