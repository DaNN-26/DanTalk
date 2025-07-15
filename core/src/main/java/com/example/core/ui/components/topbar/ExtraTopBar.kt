package com.example.core.ui.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.IconButtonWithElevation

@Composable
fun ExtraTopBar(
    navigateBack: () -> Unit,
    color: Color = DanTalkTheme.colors.oppositeTheme
) {
    IconButton(
        onClick = navigateBack,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 8.dp)
            .size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = color
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ExtraTopBar(
    actionsIcon: ImageVector,
    actions: () -> Unit,
    navigateBack: () -> Unit,
    color: Color = DanTalkTheme.colors.oppositeTheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButtonWithElevation(
            onClick = navigateBack,
            modifier = Modifier.size(50.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = color
            ),
            icon = Icons.Outlined.ArrowBackIosNew,
            contentModifier = Modifier.size(24.dp)
        )
        IconButtonWithElevation(
            onClick = actions,
            modifier = Modifier.size(50.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = color
            ),
            icon = actionsIcon,
            contentModifier = Modifier.size(24.dp)
        )
    }
}