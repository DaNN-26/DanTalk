package com.example.mvi.auth.signIn

import kotlinx.serialization.Serializable

@Serializable
data class SignInState(
    val email: String = "",
    val password: String = "",
    val validation: SignInValidation = SignInValidation.Valid,
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false
)