package com.example.feature.main.people

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.core.design.theme.DanTalkTheme
import com.example.core.ui.components.ItemShimmer
import com.example.core.ui.components.UserDialogInfo
import com.example.core.ui.components.topbar.SearchTopBar
import com.example.core.ui.model.UiUserData
import com.example.feature.main.people.component.PeopleComponent
import com.example.feature.main.people.store.PeopleStore

@Composable
fun PeopleContent(
    component: PeopleComponent,
) {
    val state by component.state.collectAsState()

    Content(
        state = state,
        onIntent = component::onIntent
    )
}

@Composable
private fun Content(
    state: PeopleStore.State,
    onIntent: (PeopleStore.Intent) -> Unit,
) {
    var dialogUserData: UiUserData? by remember { mutableStateOf(null) }
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchTopBar(
                navigateBack = { onIntent(PeopleStore.Intent.NavigateBack) },
                query = state.query,
                onQueryChange = { onIntent(PeopleStore.Intent.OnQueryChange(it)) },
            )
        },
        containerColor = DanTalkTheme.colors.singleTheme
    ) { contentPadding ->
        when {
            state.isLoading ->
                ShimmerContent(Modifier.padding(contentPadding))

            state.query.isNotEmpty() && state.usersByQuery.isEmpty() ->
                EmptyUsersContent("Ничего не найдено")

            state.query.isEmpty() ->
                EmptyUsersContent("Введите username пользователя,\nкоторого хотите найти")

            else ->
                Box {
                    if (isDialogVisible) {
                        UserDialogInfo(
                            onDismissRequest = { isDialogVisible = false },
                            onMessageSendClick = {
                                isDialogVisible = false
                                onIntent(PeopleStore.Intent.OpenChat(dialogUserData!!.id))
                            },
                            user = dialogUserData ?: UiUserData()
                        )
                    }
                    PeopleLazyColumn(
                        users = state.usersByQuery,
                        onUserClick = {
                            dialogUserData = it
                            isDialogVisible = true
                        },
                        onMessageSendClick = { onIntent(PeopleStore.Intent.OpenChat(it)) },
                        modifier = Modifier
                            .padding(contentPadding)
                            .blur(if (isDialogVisible) 3.dp else 0.dp)
                    )
                }
        }
    }
}

@Composable
private fun PeopleLazyColumn(
    users: List<UiUserData>,
    onUserClick: (UiUserData) -> Unit,
    onMessageSendClick: (String) -> Unit,
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
                onUserClick = { onUserClick(user) },
                onMessageSendClick = { onMessageSendClick(user.id) },
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
    userData: UiUserData,
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
                AsyncImage(
                    model = userData.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
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