package com.example.feature.main.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.chat.ChatItem
import com.example.core.ui.components.topbar.SearchTopBar
import com.example.feature.main.search.component.SearchComponent
import com.example.feature.main.search.store.SearchStore

@Composable
fun SearchContent(
    component: SearchComponent,
) {
    val state by component.state.collectAsState()

    Content(
        state = state,
        onIntent = component::onIntent
    )
}

@Composable
private fun Content(
    state: SearchStore.State,
    onIntent: (SearchStore.Intent) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                navigateBack = { onIntent(SearchStore.Intent.NavigateBack) },
                query = state.query,
                onQueryChange = { onIntent(SearchStore.Intent.OnQueryChange(it)) }
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(top = 8.dp)
                .imePadding()
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Чаты",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Spacer(Modifier.height(6.dp))
            }
            if (state.query.isNotEmpty())
                items(state.chatsByQuery) { chat ->
                    ChatItem(
                        onChatClick = { onIntent(SearchStore.Intent.OpenChat(chat.id)) },
                        chat = chat
                    )
                }
        }
    }
}