package com.example.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme

@Composable
fun NoInternetConnection(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = DanTalkTheme.colors.red.copy(alpha = 0.9f),
        targetValue = DanTalkTheme.colors.red.copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(8.dp)
            .background(DanTalkTheme.colors.altSingleTheme, CircleShape)
            .clickable(
                interactionSource = null,
                indication = null
            ) { expanded = !expanded }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(
                tween(200)
            ),
            exit = shrinkHorizontally(
                tween(200)
            )
        ) {
            Text(
                text = "Нет соединения",
                color = color,
                fontSize = 14.sp
            )
        }
        Icon(
            imageVector = Icons.Default.SignalWifiStatusbarConnectedNoInternet4,
            contentDescription = null,
            modifier = Modifier,
            tint = color
        )
    }
}