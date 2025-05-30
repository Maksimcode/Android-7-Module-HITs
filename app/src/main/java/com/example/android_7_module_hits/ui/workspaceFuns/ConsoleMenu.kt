package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.modalBottomSheetHeight
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.modalBottomSheetPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleMenu(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(modalBottomSheetHeight)
                .padding(modalBottomSheetPadding)
        ) {
            content()
        }
    }
}