package com.example.mvi.auth.signUp.inputEmail

sealed class InputEmailIntent {
    class OnUsernameChange(val username: String) : InputEmailIntent()
    class OnEmailChange(val email: String) : InputEmailIntent()
    data object NavigateToInputName : InputEmailIntent()
    data object NavigateBack : InputEmailIntent()
}