package com.example.mvi.auth.signIn

import kotlinx.serialization.Serializable

@Serializable
sealed class SignInValidation {
    data object Valid: SignInValidation()
    data object EmptyEmail: SignInValidation()
    data object EmptyPassword: SignInValidation()
    data object EmptyAllFields: SignInValidation()
    data object InvalidEmailFormat: SignInValidation()
    data object NetworkError: SignInValidation()
    data object InvalidCredentials: SignInValidation()
}