package com.example.dantalk.ui.view.components.topbar

import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dantalk.ui.theme.DanTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtraTopBar(
    navigateBack: () -> Unit,
    color: Color = DanTalkTheme.colors.oppositeTheme
) {
    IconButton(
        onClick = navigateBack,
        modifier = Modifier
            .padding(TopAppBarDefaults.windowInsets.asPaddingValues())
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