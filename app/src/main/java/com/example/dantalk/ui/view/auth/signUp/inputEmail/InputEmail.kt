package com.example.dantalk.ui.view.auth.signUp.inputEmail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.components.auth.signUp.inputEmail.InputEmailComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.CustomSnackbarHost
import com.example.dantalk.ui.view.components.MainTextField
import com.example.dantalk.ui.view.components.MainButton
import com.example.mvi.auth.signUp.inputEmail.InputEmailIntent
import com.example.mvi.auth.signUp.inputEmail.InputEmailValidation
import java.util.regex.Pattern

@Composable
fun InputEmail(
    component: InputEmailComponent
) {
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.validation) {
        if(state.validation != InputEmailValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (state.validation) {
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

    Content(
        snackbarHostState = snackbarHostState,
        onNextButtonClick = {
            keyboardController?.hide()
            component.processIntent(InputEmailIntent.NavigateToInputName)
        },
        username = state.username,
        onUsernameChange = { component.processIntent(InputEmailIntent.OnUsernameChange(it)) },
        isUsernameError = state.validation is InputEmailValidation.EmptyUsername
                || state.validation is InputEmailValidation.EmptyAllFields
                || state.validation is InputEmailValidation.UsernameAlreadyExist,
        email = state.email,
        onEmailChange = { component.processIntent(InputEmailIntent.OnEmailChange(it)) },
        isEmailError = state.validation is InputEmailValidation.EmptyEmail
                || state.validation is InputEmailValidation.InvalidEmailFormat
                || state.validation is InputEmailValidation.EmptyAllFields
                || state.validation is InputEmailValidation.EmailAlreadyExist,
        onBottomTextButtonClick = { component.processIntent(InputEmailIntent.NavigateBack) },
        isLoading = state.isLoading
    )
}

@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    onNextButtonClick: () -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    isUsernameError: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: Boolean,
    onBottomTextButtonClick: () -> Unit,
    isLoading: Boolean = false
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
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Регистрация",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Text(
                    text = "Создайте новый аккаунт\nи начните общаться!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.hint
                )
            }
            InputForm(
                modifier = Modifier.weight(0.3f),
                onNextButtonClick = onNextButtonClick,
                username = username,
                onUsernameChange = { onUsernameChange(it) },
                isUsernameError = isUsernameError,
                email = email,
                onEmailChange = { onEmailChange(it) },
                isEmailError = isEmailError,
                isLoading = isLoading
            )
            BottomTextButton(
                modifier = Modifier.weight(0.1f),
                onClick = onBottomTextButtonClick
            )
        }
    }
}

@Composable
private fun InputForm(
    modifier: Modifier = Modifier,
    onNextButtonClick: () -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    isUsernameError: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: Boolean,
    isLoading: Boolean
) {
    val usernamePattern = Pattern.compile("""^[_A-z0-9]*((\s)*[_A-z0-9])*${'$'}""")
    var isUsernameFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MainTextField(
            value = username,
            onValueChange = { if(it.matches(usernamePattern.toRegex())) onUsernameChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isUsernameFocused = it.isFocused },
            placeholder = "Имя пользователя",
            isError = isUsernameError,
            isLoading = isLoading,
            imeAction = ImeAction.Next
        )
        AnimatedVisibility(
            visible = isUsernameFocused,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Доступны только английские буквы, цифры или знак подчеркивания.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp,
                color = DanTalkTheme.colors.hint
            )
        }
        MainTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Email",
            isError = isEmailError,
            isLoading = isLoading,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(0.dp))
        MainButton(
            onClick = onNextButtonClick,
            modifier = Modifier.fillMaxWidth(),
            buttonText = "Далее"
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
                text = "У меня есть аккаунт",
                textAlign = TextAlign.Center,
                color = DanTalkTheme.colors.main
            )
        }
    }
}