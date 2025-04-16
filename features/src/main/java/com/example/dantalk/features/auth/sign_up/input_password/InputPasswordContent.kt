package com.example.dantalk.features.auth.sign_up.input_password

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.core.design.components.DialogueBox
import com.example.core.design.components.snackbar.CustomSnackbarHost
import com.example.core.design.components.topbar.ExtraTopBar
import com.example.dantalk.features.auth.sign_up.input_password.component.InputPasswordComponent
import com.example.dantalk.features.auth.sign_up.input_password.util.InputPasswordValidation
import com.example.dantalk.features.auth.ui.components.AuthForm
import com.example.dantalk.features.auth.ui.components.AuthFormField

@Composable
fun InputPasswordContent(
    component: InputPasswordComponent,
) {
//    val state by component.state.subscribeAsState()
//
//    Content(
//        onCompleteButtonClick = { component.processIntent(InputPasswordIntent.SignUp) },
//        password = state.password,
//        repeatablePassword = state.repeatablePassword,
//        validation = state.validation,
//        onPasswordChange = { component.processIntent(InputPasswordIntent.OnPasswordChange(it)) },
//        onRepeatablePasswordChange = { component.processIntent(InputPasswordIntent.OnRepeatablePasswordChange(it)) },
//        isLoading = state.isLoading,
//        isSuccessful = state.isSuccessful,
//        onDialogDismissRequest = { component.processIntent(InputPasswordIntent.NavigateToHome) },
//        navigateBack = { component.processIntent(InputPasswordIntent.NavigateBack) }
//    )
}

@Composable
private fun Content(
    onCompleteButtonClick: () -> Unit,
    password: String,
    repeatablePassword: String,
    validation: InputPasswordValidation,
    onPasswordChange: (String) -> Unit,
    onRepeatablePasswordChange: (String) -> Unit,
    isLoading: Boolean,
    isSuccessful: Boolean,
    onDialogDismissRequest: () -> Unit,
    navigateBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val fields = listOf(
        AuthFormField(
            title = "Пароль",
            value = password,
            onValueChange = { onPasswordChange(it) },
            isPassword = true,
            isError = validation == InputPasswordValidation.EmptyPassword
                    || validation == InputPasswordValidation.PasswordIsTooShort
                    || validation == InputPasswordValidation.NotMatchesPasswords,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
        ),
        AuthFormField(
            title = "Повторите пароль",
            value = repeatablePassword,
            onValueChange = { onRepeatablePasswordChange(it) },
            isPassword = true,
            isError = validation == InputPasswordValidation.NotMatchesPasswords,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    )

    LaunchedEffect(validation) {
        if (validation != InputPasswordValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (validation) {
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

    if (isSuccessful)
        DialogueBox(
            onDismissRequest = onDialogDismissRequest,
            text = "Вы успешно создали аккаунт!"
        )
    AuthForm(
        topBar = { ExtraTopBar(navigateBack) },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        fields = fields,
        onMainButtonClick = onCompleteButtonClick,
        title = Pair("Регистрация", "Придумайте свой пароль\n(минимум 6 символов)"),
        isLoading = isLoading,
        buttonText = "Создать аккаунт",
    )
}