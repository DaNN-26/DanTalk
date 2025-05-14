package com.example.dantalk.features.main.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.design.components.MainButton
import com.example.core.design.components.MainTextField
import com.example.core.design.theme.DanTalkTheme
import com.example.core.design.util.InputFormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
    profileFormFields: List<InputFormField>,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        containerColor = DanTalkTheme.colors.singleTheme
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Редактирование профиля",
                fontSize = 18.sp
            )
            profileFormFields.forEach { field ->
                MainTextField(
                    value = field.value,
                    onValueChange = { field.onValueChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = field.title,
                    isError = field.isError,
                    keyboardOptions = field.keyboardOptions
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            MainButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Сохранить изменения"
            )
        }
    }
}