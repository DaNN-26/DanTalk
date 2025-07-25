package com.example.feature.main.chat.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.model.UiMessage
import com.example.core.util.toDateString

@Composable
fun Message(message: UiMessage) {

    val arrangement = if (message.isCurrentUserMessage) Arrangement.End else Arrangement.Start
    val paddingValues =
        if (message.isCurrentUserMessage) PaddingValues(start = 60.dp) else PaddingValues(end = 60.dp)
    val tailAlignment =
        if (message.isCurrentUserMessage) Alignment.BottomEnd else Alignment.BottomStart
    val messageShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (message.isCurrentUserMessage) 16.dp else 0.dp,
        bottomEnd = if (message.isCurrentUserMessage) 0.dp else 16.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 8.dp),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Box(contentAlignment = tailAlignment) {
                Box(
                    modifier = Modifier
                        .padding(
                            end = if (message.isCurrentUserMessage) 6.dp else 0.dp,
                            start = if (message.isCurrentUserMessage) 0.dp else 6.dp
                        )
                        .background(
                            color = if (message.isCurrentUserMessage) DanTalkTheme.colors.main else DanTalkTheme.colors.altSingleTheme,
                            shape = messageShape
                        )
                        .padding(10.dp)
                ) {
                    Text(
                        text = message.message,
                        color = if (message.isCurrentUserMessage)
                            Color.White
                        else
                            DanTalkTheme.colors.oppositeTheme
                    )
                }
                MessageTail(
                    isCurrentUserMessage = message.isCurrentUserMessage
                )
            }
            Row(
                modifier = Modifier.align(if (message.isCurrentUserMessage) Alignment.End else Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = message.time,
                    modifier = Modifier
                        .padding(top = 3.dp),
                    color = DanTalkTheme.colors.hint,
                    fontSize = 10.sp
                )
                if (message.isCurrentUserMessage)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if(message.read) DanTalkTheme.colors.main else DanTalkTheme.colors.hint
                    )
            }
        }
    }
}

@Composable
fun MessagesDate(
    date: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = DanTalkTheme.colors.altSingleTheme,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (date != System.currentTimeMillis().toDateString())
                date else "Сегодня",
            fontWeight = FontWeight.Medium,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun MessageTail(
    isCurrentUserMessage: Boolean = false,
) {
    val tailColor =
        if (isCurrentUserMessage) DanTalkTheme.colors.main else DanTalkTheme.colors.altSingleTheme

    Canvas(
        modifier = Modifier
            .size(width = 14.dp, height = 12.dp)
            .offset(
                x = if (isCurrentUserMessage) 2.dp else (-2).dp,
                y = 0.dp
            )
    ) {
        val path = Path().apply {
            if (isCurrentUserMessage) {
                moveTo(0f, 0f)
                cubicTo(
                    x1 = 0f,
                    y1 = 0f,
                    x2 = size.width * 0.1f,
                    y2 = size.height * 0.9f,
                    x3 = 0f,
                    y3 = size.height
                )
                lineTo(size.width * 0.8f, size.height)
            } else {
                moveTo(size.width, 0f)
                cubicTo(
                    x1 = size.width,
                    y1 = 0f,
                    x2 = 0f,
                    y2 = size.height,
                    x3 = size.width * 0.2f,
                    y3 = size.height
                )
                lineTo(size.width, size.height)
            }
            close()
        }
        drawPath(path, color = tailColor)
    }
}