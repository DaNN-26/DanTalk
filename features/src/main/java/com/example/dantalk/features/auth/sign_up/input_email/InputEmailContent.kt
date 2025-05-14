package com.example.dantalk.features.auth.sign_up.input_email

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
import com.example.core.design.components.snackbar.CustomSnackbarHost
import com.example.core.design.util.InputFormField
import com.example.dantalk.features.auth.sign_up.input_email.component.InputEmailComponent
import com.example.dantalk.features.auth.sign_up.input_email.store.InputEmailStore
import com.example.dantalk.features.auth.sign_up.input_email.util.InputEmailValidation
import com.example.dantalk.features.auth.ui.components.AuthForm

@Composable
fun InputEmailContent(
    component: InputEmailComponent,
) {
    val state by component.state.collectAsState()

    Content(
        onNextButtonClick = { component.onIntent(InputEmailStore.Intent.NavigateNext) },
        username = state.username,
        email = state.email,
        validation = state.validation,
        onUsernameChange = { component.onIntent(InputEmailStore.Intent.OnUsernameChange(it)) },
        onEmailChange = { component.onIntent(InputEmailStore.Intent.OnEmailChange(it)) },
        onBottomTextButtonClick = { component.onIntent(InputEmailStore.Intent.NavigateBack) },
        isLoading = state.isLoading
    )
}

@Composable
private fun Content(
    onNextButtonClick: () -> Unit,
    username: String,
    email: String,
    validation: InputEmailValidation,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBottomTextButtonClick: () -> Unit,
    isLoading: Boolean = false,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val fields = listOf(
        InputFormField(
            title = "Email",
            value = email,
            onValueChange = { onEmailChange(it) },
            isError = validation == InputEmailValidation.EmptyEmail
                    || validation == InputEmailValidation.EmptyAllFields
                    || validation == InputEmailValidation.InvalidEmailFormat
                    || validation == InputEmailValidation.EmailAlreadyExist,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        ),
        InputFormField(
            title = "Имя пользователя",
            value = username,
            onValueChange = { onUsernameChange(it) },
            isError = validation == InputEmailValidation.EmptyUsername
                    || validation == InputEmailValidation.EmptyAllFields
                    || validation == InputEmailValidation.UsernameAlreadyExist,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )
    )

    LaunchedEffect(validation) {
        if (validation != InputEmailValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (validation) {
                    is InputEmailValidation.EmptyUsername -> "Имя пользователя не может быть пустым"
                    is InputEmailValidation.EmptyEmail -> "Почта не может быть пустой"
                    is InputEmailValidation.EmptyAllFields -> "Заполните все поля"
                    is InputEmailValidation.InvalidEmailFormat -> "Неверный формат почты"
                    is InputEmailValidation.UsernameAlreadyExist -> "Пользователь с таким именем уже существует"
                    is InputEmailValidation.EmailAlreadyExist -> "Пользователь с такой почтой уже существует"
                    else -> ""
                },
                duration = SnackbarDuration.Short
            )
        }
    }

    AuthForm(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        fields = fields,
        onMainButtonClick = onNextButtonClick,
        onBottomTextButtonClick = onBottomTextButtonClick,
        title = Pair("Регистрация", "Введите имя пользователя\nи электронную почту"),
        isLoading = isLoading,
        buttonText = "Продолжить",
        bottomButtonText = "Войти в существующий аккаунт"
    )
}