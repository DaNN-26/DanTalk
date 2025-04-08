package com.example.dantalk.ui.view.main.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.dantalk.R
import com.example.dantalk.components.main.home.HomeComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.components.chat.AnimatedChatItem
import com.example.dantalk.ui.view.components.navdrawer.NavDrawer
import com.example.dantalk.ui.view.components.topbar.MainTopBar
import com.example.mvi.main.home.HomeIntent
import kotlinx.coroutines.launch

val testChats = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "", "", "")

@Composable
fun Home(
    component: HomeComponent
) {
    val state by component.state.subscribeAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavDrawer(
        drawerState = drawerState,
        onProfileClick = { component.processIntent(HomeIntent.NavigateToProfile) },
        onPeopleClick = { /*TODO*/ },
        onSettingsClick = { /*TODO*/ },
        onInfoClick = { /*TODO*/ },
        onSignOutClick = { /*TODO*/ },
        avatar = R.drawable.ic_launcher_background,
        name = "Ermoha",
        email = "example@gmail.com"
    ) {
        Content(
            onMenuClick = {
                scope.launch {
                    drawerState.apply { if(isClosed) open() else close() }
                }
            },
            onSearchClick = { component.processIntent(HomeIntent.NavigateToSearch) },
            onChatClick = { /*TODO*/ },
            onDeleteChatClick = { /*TODO*/ },
            chats = testChats
        )
    }
}

@Composable
private fun Content(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onChatClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    chats: List<String>
) {
    Scaffold(
        topBar = {
            MainTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            items(chats) {
                AnimatedChatItem(
                    onChatClick = onChatClick,
                    onDeleteIconClick = onDeleteChatClick,
                    name = "Ermoha",
                    lastMessage = "Привет, как дела",
                    lastMessageTime = "12:13",
                    lastMessagesAmount = "1"
                )
            }
        }
    }
}