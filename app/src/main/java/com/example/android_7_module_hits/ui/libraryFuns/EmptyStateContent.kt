package com.example.android_7_module_hits.ui.libraryFuns

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.ui.theme.EmptyTextContentStyle
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.emptyImageSize

@Composable
fun EmptyStateContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty),
            contentDescription = stringResource(id = R.string.empty_illustration_description),
            modifier = Modifier.size(emptyImageSize)
        )
        Text(
            text = stringResource(id = R.string.empty_state_content_text),
            style = EmptyTextContentStyle
        )
    }
}