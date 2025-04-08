package com.example.mvi.auth.signUp.inputPassword

import kotlinx.serialization.Serializable

@Serializable
sealed class InputPasswordValidation {
    data object Valid: InputPasswordValidation()
    data object EmptyPassword: InputPasswordValidation()
    data object NotMatchesPasswords: InputPasswordValidation()
    data object PasswordIsTooShort: InputPasswordValidation()
    data object NetworkError: InputPasswordValidation()
}