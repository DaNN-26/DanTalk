package com.example.dantalk.features.auth.sign_in

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.core.design.components.DialogueBox
import com.example.core.design.components.snackbar.CustomSnackbarHost
import com.example.core.design.util.InputFormField
import com.example.dantalk.features.auth.sign_in.component.SignInComponent
import com.example.dantalk.features.auth.sign_in.store.SignInStore
import com.example.dantalk.features.auth.sign_in.util.SignInValidation
import com.example.dantalk.features.auth.ui.components.AuthForm

@Composable
fun SignInContent(
    component: SignInComponent,
) {
    val state by component.state.collectAsState()

    Content(
        onSignInButtonClick = { component.onIntent(SignInStore.Intent.SignIn) },
        email = state.email,
        password = state.password,
        validation = state.validation,
        onEmailChange = { component.onIntent(SignInStore.Intent.OnEmailChange(it)) },
        onPasswordChange = { component.onIntent(SignInStore.Intent.OnPasswordChange(it)) },
        onBottomTextButtonClick = { component.onIntent(SignInStore.Intent.NavigateToSignUp) },
        isLoading = state.isLoading,
        isSuccessful = state.isSuccessful,
        onDialogDismissRequest = { component.onIntent(SignInStore.Intent.DismissDialog) }
    )
}

@Composable
private fun Content(
    onSignInButtonClick: () -> Unit,
    email: String,
    password: String,
    validation: SignInValidation,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBottomTextButtonClick: () -> Unit,
    isLoading: Boolean,
    isSuccessful: Boolean,
    onDialogDismissRequest: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val fields = listOf(
        InputFormField(
            title = "Email",
            value = email,
            onValueChange = { onEmailChange(it) },
            isError = validation == SignInValidation.EmptyEmail
                    || validation == SignInValidation.InvalidEmailFormat
                    || validation == SignInValidation.EmptyAllFields
                    || validation == SignInValidation.InvalidCredentials,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        ),
        InputFormField(
            title = "Пароль",
            value = password,
            onValueChange = { onPasswordChange(it) },
            isPassword = true,
            isError = validation == SignInValidation.EmptyPassword
                    || validation == SignInValidation.EmptyAllFields
                    || validation == SignInValidation.InvalidCredentials,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    )

    LaunchedEffect(validation) {
        if (validation != SignInValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (validation) {
                    is SignInValidation.EmptyAllFields -> "Заполните все поля"
                    is SignInValidation.EmptyEmail -> "Почта не может быть пустой"
                    is SignInValidation.EmptyPassword -> "Пароль не может быть пустым"
                    is SignInValidation.InvalidEmailFormat -> "Неверный формат почты"
                    is SignInValidation.NetworkError -> "Проверьте подключение к сети"
                    is SignInValidation.InvalidCredentials -> "Неверные учетные данные"
                    is SignInValidation.Valid -> ""
                },
                duration = SnackbarDuration.Short
            )
        }
    }

    if (isSuccessful)
        DialogueBox(
            onDismissRequest = onDialogDismissRequest,
            text = "Вы успешно вошли в аккаунт!"
        )
    AuthForm(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        fields = fields,
        onMainButtonClick = onSignInButtonClick,
        onBottomTextButtonClick = onBottomTextButtonClick,
        title = Pair("Авторизация", "Войдите в приложение,\nчтобы общаться с друзьями!"),
        isLoading = isLoading,
        buttonText = "Войти в аккаунт",
        bottomButtonText = "Создать аккаунт"
    )
}