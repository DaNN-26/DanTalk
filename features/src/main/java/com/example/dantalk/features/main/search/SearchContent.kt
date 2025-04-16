package com.example.dantalk.features.main.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.core.design.components.chat.ChatItem
import com.example.core.design.theme.DanTalkTheme
import com.example.dantalk.R
import com.example.dantalk.features.main.search.component.SearchComponent
import com.example.dantalk.mvi.main.search.SearchIntent

val testChats = listOf("1", "2", "3")
val testContacts = listOf(1, 2, 3)

@Composable
fun SearchContent(
    component: SearchComponent
) {
    val state by component.state.subscribeAsState()

    Content(
        navigateBack = { component.processIntent(SearchIntent.NavigateBack) },
        searchQuery = state.query,
        onSearchQueryChange = { component.processIntent(SearchIntent.OnQueryChange(it)) },
        onContactClick = { /*TODO*/ },
        contacts = testContacts,
        onChatClick = { /*TODO*/ },
        chats = testChats
    )
}

@Composable
private fun Content(
    navigateBack: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onContactClick: () -> Unit,
    contacts: List<Int>,
    onChatClick: () -> Unit,
    chats: List<String>
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                navigateBack = navigateBack,
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) }
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(top = 8.dp)
        ) {
            item {
                Contacts(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onContactClick = onContactClick,
                    contacts = contacts
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                HorizontalDivider(color = DanTalkTheme.colors.hint.copy(alpha = 0.5f))
            }
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "История",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DanTalkTheme.colors.oppositeTheme
                )
                Spacer(Modifier.height(6.dp))
            }
            items(chats) {
                ChatItem(
                    onChatClick = onChatClick,
                    avatar = R.drawable.ic_launcher_background,
                    name = "Ermoha",
                    lastMessage = "Привет, как дела",
                    lastMessageTime = "12:13",
                    lastMessagesAmount = "1"
                )
            }
        }
    }
}

@Composable
private fun Contacts(
    modifier: Modifier = Modifier,
    onContactClick: () -> Unit,
    contacts: List<Int>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Контакты",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = DanTalkTheme.colors.oppositeTheme
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contacts) {
                ContactItem(onContactClick)
            }
        }
    }
}

@Composable
private fun ContactItem(
    onContactClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .clickable { onContactClick() },
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null
        )
        Text(
            text = "Ermoha",
            fontSize = 12.sp,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
}