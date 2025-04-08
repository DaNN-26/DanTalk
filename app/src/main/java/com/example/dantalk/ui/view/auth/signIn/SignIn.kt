package com.example.dantalk.ui.view.auth.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.components.auth.signIn.SignInComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.CustomSnackbarHost
import com.example.dantalk.ui.view.components.DialogueBox
import com.example.dantalk.ui.view.components.MainTextField
import com.example.dantalk.ui.view.components.MainButton
import com.example.mvi.auth.signIn.SignInIntent
import com.example.mvi.auth.signIn.SignInValidation
import com.example.mvi.auth.signUp.inputPassword.InputPasswordValidation

@Composable
fun SignIn(
    component: SignInComponent
) {
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.validation) {
        if(state.validation != SignInValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (state.validation) {
                    is SignInValidation.EmptyAllFields -> "Заполните все поля"
                    is SignInValidation.EmptyEmail -> "Почта не может быть пустой"
                    is SignInValidation.EmptyPassword -> "Пароль не может быть пустым"
                    is SignInValidation.InvalidEmailFormat -> "Неверный формат почты"
                    is SignInValidation.NetworkError -> "Проверьте подключение к сети"
                    is SignInValidation.InvalidCredentials -> "Неверные учетные данные"
                    is SignInValidation.Valid -> ""
                },
                duration = SnackbarDuration.Short,
            )
        }
    }

    Content(
        snackbarHostState = snackbarHostState,
        onSignInButtonClick = {
            keyboardController?.hide()
            component.processIntent(SignInIntent.SignIn)
        },
        email = state.email,
        onEmailChange = { component.processIntent(SignInIntent.OnEmailChange(it)) },
        password = state.password,
        onPasswordChange = { component.processIntent(SignInIntent.OnPasswordChange(it)) },
        onBottomTextButtonClick = { component.processIntent(SignInIntent.NavigateToSignUp) },
        isLoading = state.isLoading,
        isSuccessful = state.isSuccessful,
        onDialogDismissRequest = { component.processIntent(SignInIntent.NavigateToHome) }
    )
}

@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    onSignInButtonClick: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onBottomTextButtonClick: () -> Unit,
    isLoading: Boolean,
    isSuccessful: Boolean = false,
    onDialogDismissRequest: () -> Unit
) {
    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        @Suppress("UNUSED_EXPRESSION") contentPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = "Вы успешно вошли в аккаунт!"
                )
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Авторизация",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Text(
                    text = "Войдите в приложение,\nчтобы общаться с друзьями!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.hint
                )
            }
            InputForm(
                modifier = Modifier.weight(0.3f),
                onSignInButtonClick = onSignInButtonClick,
                email = email,
                onEmailChange = { onEmailChange(it) },
                password = password,
                onPasswordChange = { onPasswordChange(it) },
                isLoading = isLoading
            )
            BottomTextButton(
                onClick = onBottomTextButtonClick,
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}

@Composable
private fun InputForm(
    modifier: Modifier = Modifier,
    onSignInButtonClick: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MainTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Email",
            isLoading = isLoading,
            keyboardType = KeyboardType.Email
        )
        MainTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            isPassword = true,
            hasTrailingIcon = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Пароль",
            isLoading = isLoading,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )
        Spacer(Modifier.height(0.dp))
        MainButton(
            onClick = onSignInButtonClick,
            modifier = Modifier.fillMaxWidth(),
            buttonText = "Войти"
        )
    }
}

@Composable
private fun BottomTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TextButton(onClick = onClick) {
            Text(
                text = "У меня нет аккаунта",
                textAlign = TextAlign.Center,
                color = DanTalkTheme.colors.main
            )
        }
    }
}