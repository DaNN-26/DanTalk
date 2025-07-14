package com.example.core.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.model.UiChat

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    chat: UiChat,
    onChatClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(DanTalkTheme.colors.singleTheme)
            .clickable { onChatClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(0.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = chat.user.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = chat.user.username,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium,
                        color = DanTalkTheme.colors.oppositeTheme
                    )
                    Text(
                        text = chat.lastMessage?.time ?: "",
                        fontSize = 14.sp,
                        color = DanTalkTheme.colors.hint
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = chat.lastMessage?.message ?: "Нет сообщений",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = DanTalkTheme.colors.hint
                    )
                    chat.unreadMessagesCount.let {
                        if (it > 0) NewMessagesIndicator(it)
                    }

                    chat.lastMessage.let {
                        if (it != null && it.isCurrentUserMessage)
                            CheckMark(it.read)
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            color = DanTalkTheme.colors.hint.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun NewMessagesIndicator(
    amount: Int,
) {
    val horizontalPadding = if (amount <= 9) 10.dp else 6.dp

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(DanTalkTheme.colors.main),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = amount.toString(),
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 6.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun CheckMark(
    isRead: Boolean,
) {
    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = if (isRead) DanTalkTheme.colors.main else DanTalkTheme.colors.hint
    )
}