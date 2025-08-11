package com.example.feature.main.chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.UserDialogInfo
import com.example.data.chat.api.model.Message
import com.example.feature.main.chat.component.ChatComponent
import com.example.feature.main.chat.model.MessageListItem
import com.example.feature.main.chat.store.ChatStore
import com.example.feature.main.chat.ui.components.BottomChatBar
import com.example.feature.main.chat.ui.components.ChatTopBar
import com.example.feature.main.chat.ui.components.Message
import com.example.feature.main.chat.ui.components.MessagesDate
import kotlinx.coroutines.launch

@Composable
fun ChatContent(
    component: ChatComponent,
) {
    val state by component.state.collectAsState()

    Content(
        state = state,
        onIntent = component::onIntent
    )
}

@Composable
private fun Content(
    state: ChatStore.State,
    onIntent: (ChatStore.Intent) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val photoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) onIntent(ChatStore.Intent.SendPhoto(context, uri))
        }

    var isDialogVisible by remember { mutableStateOf(false) }

    val firstVisibleDate by remember(state.messages) {
        derivedStateOf {
            when (val firstItem = state.messages.getOrNull(lazyListState.firstVisibleItemIndex)) {
                is MessageListItem.DateItem -> firstItem.date
                is MessageListItem.MessageItem -> firstItem.message.date
                else -> ""
            }
        }
    }

    val isDateVisible by remember(state.messages) {
        derivedStateOf {
            val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf false
            val alreadyVisible = visibleItems.any {
                val item = state.messages.getOrNull(it.index)
                item is MessageListItem.DateItem && item.date == firstVisibleDate
            }
            !alreadyVisible
        }
    }

    val unreadMessageIds by remember(state.messages) {
        derivedStateOf {
            val visibleItemIndexes = lazyListState.layoutInfo.visibleItemsInfo.map { it.index }
            if (visibleItemIndexes.isEmpty()) return@derivedStateOf emptyList()

            val visibleUnreadMessages = visibleItemIndexes.mapNotNull { index ->
                val item = state.messages[index]
                if (item is MessageListItem.MessageItem && !item.message.isCurrentUserMessage && !item.message.read) item else null
            }

            if (visibleUnreadMessages.isEmpty()) return@derivedStateOf emptyList()

            val lastVisibleUnread = visibleUnreadMessages.first()
            val lastIndex = state.messages.indexOf(lastVisibleUnread)

            val messagesToMarkAsRead = state.messages
                .drop(lastIndex)
                .filterIsInstance<Message>()
                .filter { it.sender != state.currentUser.id && !it.read }
                .map { it.id }

            messagesToMarkAsRead + lastVisibleUnread.message.id
        }
    }

    LaunchedEffect(unreadMessageIds) {
        if (unreadMessageIds.isEmpty()) return@LaunchedEffect
        onIntent(ChatStore.Intent.ReadMessage(unreadMessageIds))
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                user = state.chat?.user,
                onAvatarClick = { isDialogVisible = true },
                navigateBack = { onIntent(ChatStore.Intent.NavigateBack) }
            )
        },
        bottomBar = {
            BottomChatBar(
                message = state.currentMessage,
                onMessageChange = { onIntent(ChatStore.Intent.OnMessageChange(it)) },
                sendMessage = {
                    onIntent(ChatStore.Intent.SendMessage)
                    scope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                sendPhoto = { photoLauncher.launch("image/*") }
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        if (isDialogVisible && state.chat != null)
            UserDialogInfo(
                onDismissRequest = { isDialogVisible = false },
                actionButtonContent = {
                    Text(
                        text = "Посмотреть вложения",
                        fontSize = 16.sp
                    )
                },
                onActionButtonClick = { /*TODO*/ },
                onDownloadButtonClick = {
                    onIntent(
                        ChatStore.Intent.DownloadImage(
                            context = context,
                            url = state.chat.user.avatar
                        )
                    )
                },
                user = state.chat.user
            )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (isDialogVisible) 3.dp else 0.dp)
                .padding(contentPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.chat != null) {
                if (state.messages.isEmpty()) EmptyChatContent()
                else
                    LazyColumn(
                        state = lazyListState,
                        reverseLayout = true,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(state.messages) { item ->
                            when (item) {
                                is MessageListItem.DateItem -> MessagesDate(item.date)
                                is MessageListItem.MessageItem -> Message(item.message)
                            }
                        }
                    }
                if (isDateVisible && firstVisibleDate.isNotEmpty())
                    MessagesDate(
                        date = firstVisibleDate,
                        modifier = Modifier.padding(top = 8.dp)
                    )
            } else ChatShimmerContent()
        }
    }
}

@Composable
private fun ChatShimmerContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = DanTalkTheme.colors.main
        )
    }
}

@Composable
private fun EmptyChatContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Еще нет сообщений",
            fontSize = 16.sp,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
}

@Preview
@Composable
private fun Preview() {
    DanTalkTheme {
        Content(
            state = ChatStore.State(),
            onIntent = {}
        )
    }
}