package com.example.dantalk.features.main.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.core.design.components.ItemShimmer
import com.example.core.design.components.chat.AnimatedChatItem
import com.example.core.design.theme.DanTalkTheme
import com.example.dantalk.R
import com.example.dantalk.features.main.home.component.HomeComponent
import com.example.dantalk.features.main.home.store.HomeStore
import com.example.dantalk.features.main.home.ui.components.HomeNavDrawer
import com.example.dantalk.features.main.home.ui.components.HomeTopBar
import com.example.domain.chat.model.Chat
import com.example.domain.userdata.model.UserData
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
        avatar = R.drawable.ic_launcher_background,
        user = state.user
    ) {
        Content(
            onMenuClick = {
                scope.launch {
                    drawerState.apply { if (isClosed) open() else close() }
                }
            },
            onSearchClick = { component.onIntent(HomeStore.Intent.NavigateToSearch) },
            onChatClick = { /*TODO*/ },
            onDeleteChatClick = { /*TODO*/ },
            chats = state.chats,
            currentUserData = state.user,
            isLoading = state.isLoading
        )
    }
}

@Composable
private fun Content(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onChatClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    chats: List<Chat>,
    currentUserData: UserData,
    isLoading: Boolean,
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick,
                isScrollInInitialState = {
                    lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
                }
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        if (isLoading)
            HomeShimmerContent(
                lazyListState = lazyListState,
                modifier = Modifier.padding(contentPadding)
            )
        else
            HomeLazyColumn(
                lazyListState = lazyListState,
                modifier = Modifier.padding(contentPadding),
                onChatClick = onChatClick,
                onDeleteChatClick = onDeleteChatClick,
                chats = chats,
                currentUserData = currentUserData
            )
    }
}

@Composable
private fun HomeLazyColumn(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onChatClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    chats: List<Chat>,
    currentUserData: UserData,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(chats) { chat ->
            val name = chat.users.find { it.userId != currentUserData.userId }?.username ?: ""
            val lastMessage = chat.messages.lastOrNull()?.message ?: "Нет сообщений"

            AnimatedChatItem(
                onChatClick = onChatClick,
                onDeleteIconClick = onDeleteChatClick,
                avatar = R.drawable.ic_launcher_background,
                name = name,
                lastMessage = lastMessage,
                lastMessageTime = "12:13",
                lastMessagesAmount = "1"
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