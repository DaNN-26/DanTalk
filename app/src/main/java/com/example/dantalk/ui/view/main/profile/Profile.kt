package com.example.dantalk.ui.view.main.profile

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.R
import com.example.dantalk.components.main.profile.ProfileComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.topbar.ExtraTopBar
import com.example.mvi.main.profile.ProfileIntent

@Composable
fun Profile(
    component: ProfileComponent
) {
    val state by component.state.subscribeAsState()

    Content(
        navigateBack = { component.processIntent(ProfileIntent.NavigateBack) },
        onEditButtonClick = { /*TODO*/ },
        avatar = R.drawable.ic_launcher_background,
        name = "Ermoha",
        email = "example@gmail.com",
        username = "ermohaJ"
    )
}

@Composable
private fun Content(
    navigateBack: () -> Unit,
    onEditButtonClick: () -> Unit,
    @DrawableRes avatar: Int,
    name: String,
    email: String,
    username: String
) {
    Scaffold(
        topBar = {
            ExtraTopBar(
                navigateBack = navigateBack,
                color = Color.White
            )
        },
        floatingActionButton = { ProfileEditFloatingButton { onEditButtonClick() } },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { @Suppress("UNUSED_EXPRESSION") it
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ProfileAvatar(
                avatar = avatar,
                name = name
            )
            ProfileInformation(
                email = email,
                username = username
            )
        }
    }
}

@Composable
private fun ProfileAvatar(
    @DrawableRes avatar: Int,
    name: String
) {
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
                color = if(isSystemInDarkTheme())
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
    username: String
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
    text: String
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
    onClick: () -> Unit
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