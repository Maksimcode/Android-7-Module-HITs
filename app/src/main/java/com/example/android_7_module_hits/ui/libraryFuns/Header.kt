package com.example.android_7_module_hits.ui.libraryFuns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.ui.theme.headerTextStyle
import com.example.android_7_module_hits.ui.theme.headerTitleStyle

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

        ){
        Text (
            text= stringResource(id = R.string.header_title),
            style = headerTitleStyle,
        )

        Text (
            text = stringResource(id = R.string.header_subtitle),
            style = headerTextStyle,
        )
    }
}