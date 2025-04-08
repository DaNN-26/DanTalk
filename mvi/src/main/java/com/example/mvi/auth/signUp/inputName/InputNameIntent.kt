package com.example.mvi.auth.signUp.inputName

sealed class InputNameIntent {
    class OnFirstnameChange(val firstname: String) : InputNameIntent()
    class OnLastnameChange(val lastname: String) : InputNameIntent()
    class OnPatronymicChange(val patronymic: String) : InputNameIntent()
    data object NavigateToInputPassword : InputNameIntent()
    data object NavigateBack : InputNameIntent()
}