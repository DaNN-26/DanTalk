package com.example.mvi.auth.signUp.inputName

import kotlinx.serialization.Serializable

@Serializable
data class InputNameState(
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = "",
    val isEmptyFirstname: Boolean = false
)
