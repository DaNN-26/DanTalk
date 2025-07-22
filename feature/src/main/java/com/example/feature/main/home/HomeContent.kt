package com.example.feature.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.ItemShimmer
import com.example.core.ui.components.chat.AnimatedChatItem
import com.example.core.ui.model.UiChat
import com.example.core.ui.model.UiUserData
import com.example.feature.main.home.component.HomeComponent
import com.example.feature.main.home.store.HomeStore
import com.example.feature.main.home.ui.components.HomeNavDrawer
import com.example.feature.main.home.ui.components.HomeTopBar
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    component: HomeComponent,
) {
    val state by component.state.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    HomeNavDrawer(
        drawerState = drawerState,
        onProfileClick = { component.onIntent(HomeStore.Intent.NavigateToProfile) },
        onPeopleClick = { component.onIntent(HomeStore.Intent.NavigateToPeople) },
        onSettingsClick = { /*TODO*/ },
        onInfoClick = { /*TODO*/ },
        onSignOutClick = { component.onIntent(HomeStore.Intent.SignOut) },
        user = state.user
    ) {
        Content(
            state = state,
            onIntent = component::onIntent,
            onMenuClick = {
                scope.launch {
                    drawerState.apply { if (isClosed) open() else close() }
                }
            }
        )
    }
}

@Composable
private fun Content(
    state: HomeStore.State,
    onIntent: (HomeStore.Intent) -> Unit,
    onMenuClick: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = { onIntent(HomeStore.Intent.NavigateToSearch) },
                isScrollInInitialState = {
                    lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
                }
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        if (state.isLoading)
            HomeShimmerContent(
                lazyListState = lazyListState,
                modifier = Modifier.padding(contentPadding)
            )
        else
            HomeLazyColumn(
                lazyListState = lazyListState,
                modifier = Modifier.padding(contentPadding),
                onChatClick = { onIntent(HomeStore.Intent.OpenChat(it)) },
                onDeleteClick = { onIntent(HomeStore.Intent.DeleteChat(it)) },
                chats = state.chats,
            )
    }
}

@Composable
private fun HomeLazyColumn(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onChatClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    chats: List<UiChat>,
) {
    var chatForDelete by remember { mutableStateOf<UiChat?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }

    if (isDialogVisible && chatForDelete != null)
        ChatDeleteConfirmDialog(
            onConfirm = { onDeleteClick(chatForDelete?.id ?: "") },
            onDismiss = {
                isDialogVisible = false
                chatForDelete = null
            },
            user = chatForDelete?.user ?: UiUserData()
        )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(chats) { chat ->
            AnimatedChatItem(
                chat = chat,
                onChatClick = { onChatClick(chat.id) },
                onDeleteIconClick = {
                    chatForDelete = chat
                    isDialogVisible = true
                }
            )
        }
    }
}

@Composable
private fun HomeShimmerContent(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(10) {
            ItemShimmer()
        }
    }
}

@Composable
private fun ChatDeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    user: UiUserData,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box {
            Column(
                modifier = Modifier
                    .background(
                        DanTalkTheme.colors.altSingleTheme,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Удалить чат с пользователем ${user.username}?",
                    fontSize = 18.sp,
                    color = DanTalkTheme.colors.oppositeTheme,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Это действие нельзя отменить. Вся история сообщений будет удалена.",
                    color = DanTalkTheme.colors.hint,
                    textAlign = TextAlign.Center
                )
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Подтвердить",
                        fontSize = 16.sp,
                        color = DanTalkTheme.colors.red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = DanTalkTheme.colors.hint
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}