package com.example.dantalk.features.auth.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.components.MainButton
import com.example.core.design.components.snackbar.CustomSnackbarHost
import com.example.core.design.components.topbar.ExtraTopBar
import com.example.core.design.theme.DanTalkTheme

data class AuthFormField(
    val title: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val isPassword: Boolean = false,
    val isError: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
    ),
)

@Composable
fun AuthForm(
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable () -> Unit,
    fields: List<AuthFormField>,
    onMainButtonClick: () -> Unit,
    onBottomTextButtonClick: (() -> Unit)? = null,
    title: Pair<String, String>,
    isLoading: Boolean = false,
    buttonText: String,
    bottomButtonText: String = ""
) {
    val orientation = LocalConfiguration.current.orientation
    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
    val modifier = if (isPortrait) Modifier else Modifier.verticalScroll(rememberScrollState())

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = topBar ?: {},
        snackbarHost = snackbarHost,
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(if(topBar == null) WindowInsets.systemBars else WindowInsets(0.dp))
                .padding(contentPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(if (isPortrait) 120.dp else 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(title)
                InputForm(
                    onSignInButtonClick = {
                        keyboardController?.hide()
                        onMainButtonClick()
                    },
                    fields = fields,
                    isLoading = isLoading,
                    buttonText = buttonText
                )
            }
            if (onBottomTextButtonClick != null)
                BottomTextButton(
                    onClick = onBottomTextButtonClick,
                    text = bottomButtonText
                )
        }
    }
}

@Composable
private fun Title(
    title: Pair<String, String>,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.first,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = DanTalkTheme.colors.oppositeTheme
        )
        Text(
            text = title.second,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = DanTalkTheme.colors.hint
        )
    }
}

@Composable
private fun InputForm(
    onSignInButtonClick: () -> Unit,
    fields: List<AuthFormField>,
    isLoading: Boolean,
    buttonText: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        fields.forEach { field ->
            AuthTextField(
                value = field.value,
                onValueChange = { field.onValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                isPassword = field.isPassword,
                placeholder = field.title,
                isError = field.isError,
                isLoading = isLoading,
                keyboardOptions = field.keyboardOptions
            )
        }
        Spacer(Modifier.height(0.dp))
        MainButton(
            onClick = onSignInButtonClick,
            modifier = Modifier.fillMaxWidth(),
            buttonText = buttonText
        )
    }
}

@Composable
private fun BottomTextButton(
    onClick: () -> Unit,
    text: String
) {
    Box {
        TextButton(onClick = onClick) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = DanTalkTheme.colors.main
            )
        }
    }
}