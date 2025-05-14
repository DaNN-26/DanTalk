package com.example.dantalk.features.main.people

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.components.ItemShimmer
import com.example.core.design.components.topbar.SearchTopBar
import com.example.core.design.theme.DanTalkTheme
import com.example.dantalk.R
import com.example.dantalk.features.main.people.component.PeopleComponent
import com.example.dantalk.features.main.people.store.PeopleStore
import com.example.domain.userdata.model.UserData

@Composable
fun PeopleContent(
    component: PeopleComponent,
) {
    val state by component.state.collectAsState()

    Content(
        navigateBack = { component.onIntent(PeopleStore.Intent.NavigateBack) },
        searchQuery = state.query,
        onSearchQueryChange = { component.onIntent(PeopleStore.Intent.OnQueryChange(it)) },
        onUserClick = { /*TODO*/ },
        onMessageSendClick = { /*TODO*/ },
        users = state.usersByQuery,
        isLoading = state.isLoading
    )
}

@Composable
private fun Content(
    navigateBack: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onUserClick: () -> Unit,
    onMessageSendClick: () -> Unit,
    users: List<UserData>,
    isLoading: Boolean,
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
        when {
            isLoading ->
                ShimmerContent(Modifier.padding(contentPadding))

            searchQuery.isNotEmpty() && users.isEmpty() ->
                EmptyUsersContent("Ничего не найдено")

            searchQuery.isEmpty() ->
                EmptyUsersContent("Введите username пользователя,\nкоторого хотите найти")

            else ->
                PeopleLazyColumn(
                    users = users,
                    onUserClick = onUserClick,
                    onMessageSendClick = onMessageSendClick,
                    modifier = Modifier.padding(contentPadding)
                )
        }
    }
}

@Composable
private fun PeopleLazyColumn(
    users: List<UserData>,
    onUserClick: () -> Unit,
    onMessageSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .imePadding()
    ) {
        items(users) { user ->
            UserItem(
                onUserClick = onUserClick,
                onMessageSendClick = onMessageSendClick,
                avatar = R.drawable.ic_launcher_background,
                userData = user
            )
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit,
    onMessageSendClick: () -> Unit,
    @DrawableRes avatar: Int,
    userData: UserData,
) {
    Column(
        modifier = modifier
            .background(DanTalkTheme.colors.singleTheme)
            .clickable { onUserClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(0.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = userData.firstname,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium,
                        color = DanTalkTheme.colors.oppositeTheme
                    )
                    Text(
                        text = userData.username,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = DanTalkTheme.colors.oppositeTheme
                    )
                }
            }
            IconButton(
                onClick = onMessageSendClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = DanTalkTheme.colors.main
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mail,
                    contentDescription = null
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            color = DanTalkTheme.colors.hint.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun EmptyUsersContent(
    text: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = DanTalkTheme.colors.oppositeTheme
        )
    }
}

@Composable
private fun ShimmerContent(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .imePadding()
    ) {
        items(10) {
            ItemShimmer()
        }
    }
}