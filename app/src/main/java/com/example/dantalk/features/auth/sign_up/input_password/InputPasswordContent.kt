package com.example.dantalk.features.auth.sign_up.input_password

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
import com.example.dantalk.design.components.DialogueBox
import com.example.dantalk.design.components.snackbar.CustomSnackbarHost
import com.example.dantalk.design.components.topbar.ExtraTopBar
import com.example.dantalk.design.util.InputFormField
import com.example.dantalk.features.auth.sign_up.input_password.component.InputPasswordComponent
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore
import com.example.dantalk.features.auth.sign_up.input_password.util.InputPasswordValidation
import com.example.dantalk.features.auth.ui.components.AuthForm

@Composable
fun InputPasswordContent(
    component: InputPasswordComponent,
) {
    val state by component.state.collectAsState()

    Content(
        state = state,
        onIntent = { component.onIntent(it) }
    )
}

@Composable
private fun Content(
    state: InputPasswordStore.State,
    onIntent: (InputPasswordStore.Intent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val fields = listOf(
        InputFormField(
            title = "Пароль",
            value = state.password,
            onValueChange = { onIntent(InputPasswordStore.Intent.OnPasswordChange(it)) },
            isPassword = true,
            isError = state.validation == InputPasswordValidation.EmptyPassword
                    || state.validation == InputPasswordValidation.PasswordIsTooShort
                    || state.validation == InputPasswordValidation.NotMatchesPasswords,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
        ),
        InputFormField(
            title = "Повторите пароль",
            value = state.repeatablePassword,
            onValueChange = { onIntent(InputPasswordStore.Intent.OnRepeatablePasswordChange(it)) },
            isPassword = true,
            isError = state.validation == InputPasswordValidation.NotMatchesPasswords,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    )

    LaunchedEffect(state.validation) {
        if (state.validation != InputPasswordValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (state.validation) {
                    is InputPasswordValidation.EmptyPassword -> "Пароль не может быть пустым"
                    is InputPasswordValidation.NotMatchesPasswords -> "Пароли не совпадают"
                    is InputPasswordValidation.PasswordIsTooShort -> "Пароль должен содержать минимум 6 символов"
                    is InputPasswordValidation.NetworkError -> "Проверьте подключение к сети"
                    is InputPasswordValidation.Valid -> ""
                },
                duration = SnackbarDuration.Short,
            )
        }
    }

    if (state.isSuccessful)
        DialogueBox(
            onDismissRequest = { onIntent(InputPasswordStore.Intent.DismissDialog) },
            text = "Вы успешно создали аккаунт!"
        )
    AuthForm(
        topBar = {
            ExtraTopBar(
                navigateBack = { onIntent(InputPasswordStore.Intent.NavigateBack) },
            )
        },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        fields = fields,
        onMainButtonClick = { onIntent(InputPasswordStore.Intent.SignUp) },
        title = Pair("Регистрация", "Придумайте свой пароль\n(минимум 6 символов)"),
        isLoading = state.isLoading,
        buttonText = "Создать аккаунт",
    )
}