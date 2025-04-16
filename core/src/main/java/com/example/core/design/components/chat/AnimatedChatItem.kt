package com.example.core.design.components.chat

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.core.design.theme.DanTalkTheme
import kotlin.math.roundToInt

private enum class SwipeAnchors {
    Center, Right
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedChatItem(
    onChatClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    @DrawableRes avatar: Int,
    name: String,
    lastMessage: String,
    lastMessageTime: String,
    lastMessagesAmount: String
) {
    val swipeState = remember {
        AnchoredDraggableState(
            initialValue = SwipeAnchors.Center,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 100f },
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            ),
            decayAnimationSpec = exponentialDecay()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    SwipeAnchors.Center at 0f
                    SwipeAnchors.Right at -200f
                }
            )
        }
    }
    val animatedBoxColor: Color by animateColorAsState(
        targetValue = if(swipeState.currentValue == SwipeAnchors.Right)
            DanTalkTheme.colors.red
        else
            DanTalkTheme.colors.singleTheme,
        animationSpec = tween(200)
    )
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedChatItemAction(
            onDeleteIconClick = onDeleteIconClick,
            boxColor = animatedBoxColor
        )
        ChatItem(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = swipeState.offset.roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = swipeState,
                    orientation = Orientation.Horizontal
                ),
            onChatClick = onChatClick,
            avatar = avatar,
            name = name,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime,
            lastMessagesAmount = lastMessagesAmount
        )
    }
}

@Composable
private fun AnimatedChatItemAction(
    onDeleteIconClick: () -> Unit,
    boxColor: Color
) {
    Box(
        modifier = Modifier
            .background(boxColor)
            .padding(16.dp)
    ) {
        IconButton(
            onClick = onDeleteIconClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null
            )
        }
    }
}