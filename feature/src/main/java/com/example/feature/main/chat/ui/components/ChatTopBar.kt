package com.example.feature.main.chat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme
import com.example.feature.R
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    title: String?,
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            if (title != null)
                Text(
                    text = title,
                    fontSize = 20.sp
                )
            else
                Box(
                    modifier = Modifier
                        .shimmer()
                        .size(width = 100.dp, height = 20.dp)
                        .background(DanTalkTheme.colors.hint.copy(alpha = 0.4f))
                )
        },
        modifier = Modifier.shadow(4.dp),
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                if (title != null)
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null
                    )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DanTalkTheme.colors.altSingleTheme,
            titleContentColor = DanTalkTheme.colors.oppositeTheme,
            navigationIconContentColor = DanTalkTheme.colors.oppositeTheme
        )
    )
}