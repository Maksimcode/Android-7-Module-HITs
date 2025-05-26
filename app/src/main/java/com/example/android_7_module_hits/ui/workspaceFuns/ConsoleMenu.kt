package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleMenu(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = { /* Если консольный вывод не будет влезать, эта штука поможет сделать нам "ручку" для расщирения окна */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        ) {
            content()
        }
    }
}