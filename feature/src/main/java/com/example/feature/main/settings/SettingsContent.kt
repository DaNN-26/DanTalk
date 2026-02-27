package com.example.feature.main.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.DesignServices
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.topbar.ExtraTopBar
import com.example.feature.R
import com.example.feature.main.settings.component.SettingsComponent
import com.example.feature.main.settings.ui.components.SettingsTopBar

private data class SettingsItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun SettingsContent(
    component: SettingsComponent
) {
    Content()
}

@Composable
private fun Content() {
    val items = listOf(
        SettingsItem(
            icon = Icons.Outlined.DesignServices,
            title = "Настройки темы",
            onClick = { /*TODO*/ }
        ),
        SettingsItem(
            icon = Icons.Outlined.Notifications,
            title = "Уведомления",
            onClick = {}
        )
    )

    Scaffold(
        topBar = { SettingsTopBar { /*TODO*/ } },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Header()
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                items(items) { item ->
                    Item(item)
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape)
        )
        Text(
            text = "Username",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
}

@Composable
private fun Item(
    item: SettingsItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { item.onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = DanTalkTheme.colors.hint
        )
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
}

@Composable
@Preview
private fun Preview() {
    DanTalkTheme(darkTheme = true) {
        Content()
    }
}