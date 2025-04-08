package com.example.mvi.auth.signUp.inputEmail

import kotlinx.serialization.Serializable

@Serializable
data class InputEmailState(
    val username: String = "",
    val email: String = "",
    val validation: InputEmailValidation = InputEmailValidation.Valid,
    val isLoading: Boolean = false
)
