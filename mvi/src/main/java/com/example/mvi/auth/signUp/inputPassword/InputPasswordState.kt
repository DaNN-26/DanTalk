package com.example.mvi.auth.signUp.inputPassword

import kotlinx.serialization.Serializable

@Serializable
data class InputPasswordState(
    val password: String = "",
    val repeatablePassword: String = "",
    val validation: InputPasswordValidation = InputPasswordValidation.Valid,
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false
)
