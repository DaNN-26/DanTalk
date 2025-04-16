package com.example.dantalk.features.main.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme

@Composable
fun HomeNavDrawer(
    drawerState: DrawerState,
    onProfileClick: () -> Unit,
    onPeopleClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    onSignOutClick: () -> Unit,
    @DrawableRes avatar: Int,
    name: String,
    email: String,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.85f),
                drawerShape = RoundedCornerShape(topEnd = 24.dp),
                drawerContainerColor = DanTalkTheme.colors.singleTheme
            ) {
                NavDrawerHeader(
                    avatar = avatar,
                    name = name,
                    email = email
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        NavDrawerItem(
                            onClick = onProfileClick,
                            text = "Профиль",
                            icon = Icons.Outlined.AccountCircle
                        )
                        NavDrawerItem(
                            onClick = onPeopleClick,
                            text = "Люди",
                            icon = Icons.Outlined.People
                        )
                        HorizontalDivider()
                        NavDrawerItem(
                            onClick = onSettingsClick,
                            text = "Настройки",
                            icon = Icons.Outlined.Settings
                        )
                        NavDrawerItem(
                            onClick = onInfoClick,
                            text = "Справка",
                            icon = Icons.Outlined.Info
                        )
                    }
                    NavDrawerItem(
                        onClick = onSignOutClick,
                        text = "Выйти из аккаунта",
                        icon = Icons.AutoMirrored.Outlined.Logout
                    )
                }
            }
        },
        gesturesEnabled = true,
        content = content
    )
}

@Composable
private fun NavDrawerHeader(
    @DrawableRes avatar: Int,
    name: String,
    email: String
) {
    val gradient = if(!isSystemInDarkTheme())
        Brush.linearGradient(
            listOf(
                Color(0, 183, 225),
                Color(41, 214, 212)
            )
        )
    else
        Brush.linearGradient(
            listOf(
                Color(10, 21, 37, 255),
                Color(4, 31, 61, 255)
            )
        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
    ) {
        Image(
            painter = painterResource(avatar),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            text = email,
            color = Color.White
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun NavDrawerItem(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                color = DanTalkTheme.colors.oppositeTheme
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DanTalkTheme.colors.hint
            )
        },
        selected = false,
        shape = RectangleShape,
        onClick = onClick
    )
}