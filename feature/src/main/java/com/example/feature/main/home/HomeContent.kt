package com.example.feature.main.home

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
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.ItemShimmer
import com.example.core.ui.components.chat.AnimatedChatItem
import com.example.core.ui.model.UiChat
import com.example.feature.R
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
                onDeleteChatClick = { /*TODO*/ },
                chats = state.chats,
            )
    }
}

@Composable
private fun HomeLazyColumn(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onChatClick: (String) -> Unit,
    onDeleteChatClick: () -> Unit,
    chats: List<UiChat>,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(chats) { chat ->
            AnimatedChatItem(
                chat = chat,
                onChatClick = { onChatClick(chat.id) },
                onDeleteIconClick = onDeleteChatClick
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