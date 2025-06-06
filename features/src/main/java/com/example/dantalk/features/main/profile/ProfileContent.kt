package com.example.dantalk.features.main.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.components.snackbar.CustomSnackbarHost
import com.example.core.design.components.topbar.ExtraTopBar
import com.example.core.design.theme.DanTalkTheme
import com.example.core.design.util.InputFormField
import com.example.dantalk.R
import com.example.dantalk.features.main.profile.component.ProfileComponent
import com.example.dantalk.features.main.profile.store.ProfileStore
import com.example.dantalk.features.main.profile.ui.components.ProfileBottomSheet
import com.example.dantalk.features.main.profile.util.ProfileValidation
import com.example.domain.userdata.model.UserData

@Composable
fun ProfileContent(
    component: ProfileComponent,
) {
    val state by component.state.collectAsState()

    Content(
        navigateBack = { component.onIntent(ProfileStore.Intent.NavigateBack) },
        avatar = R.drawable.ic_launcher_background,
        userData = state.currentUser ?: UserData(),
        saveUserData = { component.onIntent(ProfileStore.Intent.SaveNewUserData) },
        fieldsUserData = state.newUserData,
        onUpdateUserData = { component.onIntent(ProfileStore.Intent.UpdateNewUserData(it)) },
        validation = state.validation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    navigateBack: () -> Unit,
    @DrawableRes avatar: Int,
    userData: UserData,
    saveUserData: () -> Unit,
    fieldsUserData: UserData,
    onUpdateUserData: (UserData) -> Unit,
    validation: ProfileValidation,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState(true)
    var sheetIsVisible by remember { mutableStateOf(false) }

    val fields = listOf(
        InputFormField(
            title = "Имя",
            value = fieldsUserData.firstname,
            onValueChange = { onUpdateUserData(fieldsUserData.copy(firstname = it)) },
            isError = validation == ProfileValidation.EmptyFirstname,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        ),
        InputFormField(
            title = "Фамилия",
            value = fieldsUserData.lastname,
            onValueChange = { onUpdateUserData(fieldsUserData.copy(lastname = it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        ),
        InputFormField(
            title = "Отчество",
            value = fieldsUserData.patronymic,
            onValueChange = { onUpdateUserData(fieldsUserData.copy(patronymic = it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        ),
        InputFormField(
            title = "Электронная почта",
            value = fieldsUserData.email,
            onValueChange = { onUpdateUserData(fieldsUserData.copy(email = it)) },
            isError = validation == ProfileValidation.EmptyEmail
                    || validation == ProfileValidation.InvalidEmailFormat
                    || validation == ProfileValidation.EmailExists,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        ),
        InputFormField(
            title = "Имя пользователя",
            value = fieldsUserData.username,
            onValueChange = { onUpdateUserData(fieldsUserData.copy(username = it)) },
            isError = validation == ProfileValidation.EmptyUsername
                    || validation == ProfileValidation.UsernameExists,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    )

    LaunchedEffect(Unit) {
        if (validation != ProfileValidation.Valid) {
            snackbarHostState.showSnackbar(
                message = when (validation) {
                    ProfileValidation.EmptyFirstname -> "Поле имени не должно быть пустым"
                    ProfileValidation.EmptyEmail -> "Поле электронной почты не должно быть пустым"
                    ProfileValidation.InvalidEmailFormat -> "Неверный формат электронной почты"
                    ProfileValidation.EmptyUsername -> "Поле имени пользователя не должно быть пустым"
                    ProfileValidation.UsernameExists -> "Имя пользователя уже существует"
                    ProfileValidation.EmailExists -> "Электронная почта уже существует"
                    ProfileValidation.Valid -> ""
                }
            )
        }
    }

    Scaffold(
        topBar = {
            ExtraTopBar(
                navigateBack = navigateBack,
                color = Color.White
            )
        },
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            ProfileEditFloatingButton { sheetIsVisible = true }
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) {
        @Suppress("UNUSED_EXPRESSION") it
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ProfileAvatar(
                avatar = avatar,
                firstname = userData.firstname,
                lastname = userData.lastname,
                patronymic = userData.patronymic
            )
            ProfileInformation(
                email = userData.email,
                username = userData.username
            )
            if (sheetIsVisible)
                ProfileBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { sheetIsVisible = false },
                    onClick = {
                        saveUserData()
                        sheetIsVisible = false
                    },
                    profileFormFields = fields
                )
        }
    }
}

@Composable
private fun ProfileAvatar(
    @DrawableRes avatar: Int,
    firstname: String,
    lastname: String,
    patronymic: String,
) {
    val name = if ((lastname.isBlank() && patronymic.isBlank()))
        firstname
    else if (patronymic.isBlank())
        firstname + "\n" + lastname
    else if (lastname.isBlank())
        firstname + "\n" + patronymic
    else
        firstname + "\n" + lastname + "\n" + patronymic
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp)
    ) {
        Image(
            painter = painterResource(avatar),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                color = if (isSystemInDarkTheme())
                    Color(211, 211, 211, 255)
                else
                    Color.White,
                blendMode = BlendMode.Darken
            )
        )
        Text(
            text = name,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.7f),
                    blurRadius = 100f
                )
            )
        )
    }
}

@Composable
private fun ProfileInformation(
    email: String,
    username: String,
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Информация",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DanTalkTheme.colors.main
        )
        InformationItem(
            icon = Icons.Outlined.Email,
            title = "Электронная почта",
            text = email
        )
        InformationItem(
            icon = Icons.Outlined.Person,
            title = "Имя пользователя",
            text = username
        )
    }
}

@Composable
private fun InformationItem(
    icon: ImageVector,
    title: String,
    text: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DanTalkTheme.colors.oppositeTheme
        )
        VerticalDivider(
            modifier = Modifier
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            thickness = 2.dp,
            color = DanTalkTheme.colors.oppositeTheme
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = DanTalkTheme.colors.oppositeTheme
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = DanTalkTheme.colors.hint
            )
        }
    }
}

@Composable
private fun ProfileEditFloatingButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(65.dp),
        shape = CircleShape,
        containerColor = DanTalkTheme.colors.main,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}