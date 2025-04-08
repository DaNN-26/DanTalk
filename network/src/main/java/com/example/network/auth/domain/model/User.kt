package com.example.network.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = ""
)
