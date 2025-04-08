package com.example.mvi.auth.signIn

sealed class SignInIntent {
    class OnEmailChange(val email: String) : SignInIntent()
    class OnPasswordChange(val password: String) : SignInIntent()
    data object SignIn : SignInIntent()
    data object NavigateToHome : SignInIntent()
    data object NavigateToSignUp : SignInIntent()
}