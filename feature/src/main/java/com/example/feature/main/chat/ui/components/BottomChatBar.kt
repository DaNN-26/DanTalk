package com.example.feature.main.chat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme

@Composable
fun BottomChatBar(
    message: String,
    onMessageChange: (String) -> Unit,
    sendMessage: () -> Unit,
    sendPhoto: () -> Unit
) {
    BasicTextField(
        value = message,
        onValueChange = { onMessageChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(DanTalkTheme.colors.altSingleTheme)
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .navigationBarsPadding()
            .imePadding(),
        textStyle = LocalTextStyle.current.copy(
            color = DanTalkTheme.colors.oppositeTheme,
            fontSize = 18.sp
        ),
        maxLines = 4,
        cursorBrush = SolidColor(DanTalkTheme.colors.main),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (message.isEmpty())
                        Text(
                            text = "Сообщение",
                            fontSize = 18.sp,
                            color = DanTalkTheme.colors.subText
                        )
                    innerTextField()
                }
                Row {
                    IconButton(
                        onClick = sendPhoto,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = DanTalkTheme.colors.oppositeTheme
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                    AnimatedVisibility(
                        visible = message.isNotBlank(),
                    ) {
                        IconButton(
                            onClick = sendMessage,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = DanTalkTheme.colors.oppositeTheme
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    )
}