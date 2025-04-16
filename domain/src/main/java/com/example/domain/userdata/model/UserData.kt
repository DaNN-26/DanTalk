package com.example.domain.userdata.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = ""
)
