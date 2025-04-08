package com.example.dantalk.ui.view.auth.signUp.inputPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.components.auth.signUp.inputPassword.InputPasswordComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.CustomSnackbarHost
import com.example.dantalk.ui.view.components.DialogueBox
import com.example.dantalk.ui.view.components.MainButton
import com.example.dantalk.ui.view.components.MainTextField
import com.example.dantalk.ui.view.components.topbar.ExtraTopBar
import com.example.mvi.auth.signUp.inputPassword.InputPasswordIntent
import com.example.mvi.auth.signUp.inputPassword.InputPasswordValidation

@Composable
fun InputPassword(
    component: InputPasswordComponent
) {
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.validation) {
        if(state.validation != InputPasswordValidation.Valid) {
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

    Content(
        snackbarHostState = snackbarHostState,
        navigateBack = { component.processIntent(InputPasswordIntent.NavigateBack) },
        onCompleteButtonClick = {
            keyboardController?.hide()
            component.processIntent(InputPasswordIntent.SignUp)
        },
        password = state.password,
        onPasswordChange = { component.processIntent(InputPasswordIntent.OnPasswordChange(it)) },
        isPasswordError = state.validation == InputPasswordValidation.EmptyPassword ||
                state.validation == InputPasswordValidation.NotMatchesPasswords ||
                state.validation == InputPasswordValidation.PasswordIsTooShort,
        repeatablePassword = state.repeatablePassword,
        onRepeatablePasswordChange = { component.processIntent(InputPasswordIntent.OnRepeatablePasswordChange(it)) },
        isRepeatablePasswordError = state.validation == InputPasswordValidation.NotMatchesPasswords,
        isLoading = state.isLoading,
        isSuccessful = state.isSuccessful,
        onDialogDismissRequest = { component.processIntent(InputPasswordIntent.NavigateToHome) }
    )
}

@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    onCompleteButtonClick: () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordError: Boolean,
    repeatablePassword: String,
    onRepeatablePasswordChange: (String) -> Unit,
    isRepeatablePasswordError: Boolean,
    isLoading: Boolean,
    isSuccessful: Boolean,
    onDialogDismissRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            ExtraTopBar(
                navigateBack = navigateBack
            )
        },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        @Suppress("UNUSED_EXPRESSION") contentPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(if(isSuccessful) 10.dp else 0.dp)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 30.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isSuccessful)
                DialogueBox(
                    onDismissRequest = onDialogDismissRequest,
                    text = "Вы успешно создали аккаунт!"
                )
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Пароль",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Text(
                    text = "Придумайте надежный и запоминающийся пароль, чтобы защитить свой аккаунт",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.hint
                )
            }
            InputForm(
                modifier = Modifier.weight(0.4f),
                onCompleteButtonClick = onCompleteButtonClick,
                password = password,
                onPasswordChange = { onPasswordChange(it) },
                isPasswordError = isPasswordError,
                repeatablePassword = repeatablePassword,
                onRepeatablePasswordChange = { onRepeatablePasswordChange(it) },
                isRepeatablePasswordError = isRepeatablePasswordError,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun InputForm(
    modifier: Modifier = Modifier,
    onCompleteButtonClick: () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordError: Boolean,
    repeatablePassword: String,
    onRepeatablePasswordChange: (String) -> Unit,
    isRepeatablePasswordError: Boolean,
    isLoading: Boolean
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MainTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            hasTrailingIcon = !isLoading,
            placeholder = "Пароль",
            isError = isPasswordError,
            isLoading = isLoading,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        )
        MainTextField(
            value = repeatablePassword,
            onValueChange = { onRepeatablePasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            placeholder = "Повторите пароль",
            isError = isRepeatablePasswordError,
            isLoading = isLoading,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )
        Spacer(Modifier.height(0.dp))
        MainButton(
            onClick = onCompleteButtonClick,
            modifier = Modifier.fillMaxWidth(),
            buttonText = "Завершить регистрацию"
        )
    }
}