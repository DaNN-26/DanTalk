package com.example.dantalk.features.auth.sign_up.input_name

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
import com.example.core.design.components.topbar.ExtraTopBar
import com.example.dantalk.features.auth.sign_up.input_name.component.InputNameComponent
import com.example.dantalk.features.auth.sign_up.input_name.store.InputNameStore
import com.example.dantalk.features.auth.ui.components.AuthForm
import com.example.dantalk.features.auth.ui.components.AuthFormField

@Composable
fun InputNameContent(
    component: InputNameComponent,
) {
    val state by component.state.collectAsState()

    Content(
        onNextButtonClick = { component.onIntent(InputNameStore.Intent.NavigateNext) },
        firstname = state.firstname,
        lastname = state.lastname,
        patronymic = state.patronymic,
        onFirstnameChange = { component.onIntent(InputNameStore.Intent.OnFirstnameChange(it)) },
        onLastnameChange = { component.onIntent(InputNameStore.Intent.OnLastnameChange(it)) },
        onPatronymicChange = { component.onIntent(InputNameStore.Intent.OnPatronymicChange(it)) },
        isFirstnameError = state.isEmptyFirstname,
        navigateBack = { component.onIntent(InputNameStore.Intent.NavigateBack) }
    )
}

@Composable
private fun Content(
    onNextButtonClick: () -> Unit,
    firstname: String,
    lastname: String,
    patronymic: String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onPatronymicChange: (String) -> Unit,
    isFirstnameError: Boolean,
    navigateBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val fields = listOf(
        AuthFormField(
            title = "Имя",
            value = firstname,
            onValueChange = { onFirstnameChange(it) },
            isError = isFirstnameError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )
        ),
        AuthFormField(
            title = "Фамилия (опционально)",
            value = lastname,
            onValueChange = { onLastnameChange(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )
        ),
        AuthFormField(
            title = "Отчество (опционально)",
            value = patronymic,
            onValueChange = { onPatronymicChange(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )
    )

    LaunchedEffect(isFirstnameError) {
        if (isFirstnameError)
            snackbarHostState.showSnackbar(
                message = "Имя не может быть пустым",
                duration = SnackbarDuration.Short
            )
    }

    AuthForm(
        topBar = { ExtraTopBar(navigateBack) },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        fields = fields,
        onMainButtonClick = onNextButtonClick,
        title = Pair("Регистрация", "Введите данные о себе"),
        buttonText = "Продолжить"
    )
}