package com.example.dantalk.features.main.home.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.only
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.dantalk.design.theme.DanTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    isScrollInInitialState: () -> Boolean,
) {
    val elevation = if (isScrollInInitialState.invoke()) 0.dp else 4.dp
    val topBarColor = if (isScrollInInitialState.invoke())
        DanTalkTheme.colors.singleTheme else DanTalkTheme.colors.topBar

    TopAppBar(
        title = {},
        modifier = Modifier.shadow(elevation),
        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSearchClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topBarColor,
            actionIconContentColor = DanTalkTheme.colors.oppositeTheme,
            navigationIconContentColor = DanTalkTheme.colors.oppositeTheme
        ),
        windowInsets = TopAppBarDefaults.windowInsets
            .only(WindowInsetsSides.Top)
            .add(WindowInsets(left = 4.dp, right = 4.dp))
    )
}