package com.example.mvi.auth.signUp.inputEmail

import kotlinx.serialization.Serializable

@Serializable
sealed class InputEmailValidation {
    data object Valid: InputEmailValidation()
    data object EmptyUsername: InputEmailValidation()
    data object EmptyEmail: InputEmailValidation()
    data object EmptyAllFields: InputEmailValidation()
    data object UsernameAlreadyExist: InputEmailValidation()
    data object EmailAlreadyExist: InputEmailValidation()
    data object InvalidEmailFormat: InputEmailValidation()
}