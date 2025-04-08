package com.example.dantalk.ui.view.auth.signUp.inputName

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.components.auth.signUp.inputName.InputNameComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.CustomSnackbarHost
import com.example.dantalk.ui.view.components.MainButton
import com.example.dantalk.ui.view.components.MainTextField
import com.example.dantalk.ui.view.components.topbar.ExtraTopBar
import com.example.mvi.auth.signUp.inputName.InputNameIntent

@Composable
fun InputName(
    component: InputNameComponent
) {
    val state by component.state.subscribeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isEmptyFirstname) {
        if(state.isEmptyFirstname)
            snackbarHostState.showSnackbar(
                message = "Имя не может быть пустым",
                duration = SnackbarDuration.Short
            )
    }

    Content(
        snackbarHostState = snackbarHostState,
        navigateBack = { component.processIntent(InputNameIntent.NavigateBack) },
        onNextButtonClick = {
            keyboardController?.hide()
            component.processIntent(InputNameIntent.NavigateToInputPassword)
        },
        firstname = state.firstname,
        onFirstnameChange = { component.processIntent(InputNameIntent.OnFirstnameChange(it)) },
        isFirstnameError = state.isEmptyFirstname,
        lastname = state.lastname,
        onLastnameChange = { component.processIntent(InputNameIntent.OnLastnameChange(it)) },
        patronymic = state.patronymic,
        onPatronymicChange = { component.processIntent(InputNameIntent.OnPatronymicChange(it)) }
    )
}

@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    onNextButtonClick: () -> Unit,
    firstname: String,
    onFirstnameChange: (String) -> Unit,
    isFirstnameError: Boolean,
    lastname: String,
    onLastnameChange: (String) -> Unit,
    patronymic: String,
    onPatronymicChange: (String) -> Unit
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Информация",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Text(
                    text = "Введите основную информацию о себе",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = DanTalkTheme.colors.hint
                )
            }
            InputForm(
                modifier = Modifier.weight(0.5f),
                onNextButtonClick = onNextButtonClick,
                firstname = firstname,
                onFirstnameChange = { onFirstnameChange(it) },
                isFirstnameError = isFirstnameError,
                lastname = lastname,
                onLastnameChange = { onLastnameChange(it) },
                patronymic = patronymic,
                onPatronymicChange = { onPatronymicChange(it) }
            )
        }
    }
}

@Composable
private fun InputForm(
    modifier: Modifier = Modifier,
    onNextButtonClick: () -> Unit,
    firstname: String,
    onFirstnameChange: (String) -> Unit,
    isFirstnameError: Boolean,
    lastname: String,
    onLastnameChange: (String) -> Unit,
    patronymic: String,
    onPatronymicChange: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MainTextField(
            value = firstname,
            onValueChange = { onFirstnameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Имя",
            isError = isFirstnameError,
            imeAction = ImeAction.Next
        )
        MainTextField(
            value = lastname,
            onValueChange = { onLastnameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Фамилия (опционально)",
            imeAction = ImeAction.Next
        )
        MainTextField(
            value = patronymic,
            onValueChange = { onPatronymicChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Отчество (опционально)",
            imeAction = ImeAction.Done
        )
        Spacer(Modifier.height(0.dp))
        MainButton(
            onClick = onNextButtonClick,
            modifier = Modifier.fillMaxWidth(),
            buttonText = "Далее"
        )
    }
}