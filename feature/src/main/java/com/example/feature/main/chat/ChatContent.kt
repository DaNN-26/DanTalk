package com.example.feature.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.main.chat.component.ChatComponent
import com.example.data.chat.api.model.Message
import com.example.feature.R

@Composable
fun ChatContent(
    component: ChatComponent,
) {
    val state by component.state.collectAsState()

    Content(
    )
}

@Composable
private fun Content(
) {
    val testMessages = listOf(
        Message(
            id = "1",
            sender = "1",
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam aliquam neque sed dui hendrerit, quis euismod urna suscipit. Praesent ut lectus ut dolor iaculis blandit. Morbi nec purus at enim blandit blandit."
        ),
        Message(
            id = "2",
            sender = "2",
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam aliquam neque sed dui hendrerit, quis euismod urna suscipit. Praesent ut lectus ut dolor iaculis blandit. Morbi nec purus at enim blandit blandit."
        ),
        Message(
            id = "3",
            sender = "1",
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam aliquam neque sed dui hendrerit, quis euismod urna suscipit. Praesent ut lectus ut dolor iaculis blandit. Morbi nec purus at enim blandit blandit."
        )
    )

    Scaffold(
        topBar = {
            ChatTopBar(
                title = "Никнейм",
                navigateBack = { }
            )
        },
        bottomBar = {
            BottomChatBar()
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding
        ) {
            items(testMessages) { message ->
                Message(
                    message = message,
                    currentUserId = "2"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    title: String,
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = navigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun Message(
    message: Message,
    currentUserId: String,
) {
    val isCurrentUsersMessage = currentUserId == message.sender
    val modifier = if (isCurrentUsersMessage)
        Modifier
            .padding(end = 20.dp)
            .background(
                Color.Gray,
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
    else
        Modifier
            .padding(start = 20.dp)
            .background(
                Color.LightGray,
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )
            )
    Box(
        modifier = modifier
    ) {
        Text(
            text = message.message,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun BottomChatBar() {

}

@Composable
@Preview
private fun Preview() {
    Content(
    )
}