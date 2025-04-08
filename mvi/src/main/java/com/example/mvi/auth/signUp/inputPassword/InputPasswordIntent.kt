package com.example.mvi.auth.signUp.inputPassword

sealed class InputPasswordIntent {
    class OnPasswordChange(val password: String) : InputPasswordIntent()
    class OnRepeatablePasswordChange(val repeatablePassword: String) : InputPasswordIntent()
    data object SignUp : InputPasswordIntent()
    data object NavigateToHome : InputPasswordIntent()
    data object NavigateBack : InputPasswordIntent()
}