package com.example.dantalk.ui.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dantalk.ui.theme.DanTalkTheme

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
    ) { snackbarData ->
        Snackbar(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = DanTalkTheme.colors.altSingleTheme,
            contentColor = DanTalkTheme.colors.oppositeTheme
        ) {
            Text(text = snackbarData.visuals.message)
        }
    }
}