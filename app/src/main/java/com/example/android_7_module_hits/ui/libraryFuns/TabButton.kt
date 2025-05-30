package com.example.android_7_module_hits.ui.libraryFuns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.shadowElevation
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.tabButtonCornerHeight
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.tabButtonCornerRadius
import com.example.android_7_module_hits.ui.theme.NotSelectedTabColor
import com.example.android_7_module_hits.ui.theme.SelectedTabColor
import com.example.android_7_module_hits.ui.theme.ShadowColor
import com.example.android_7_module_hits.ui.theme.TabButtonTextStyle

@Composable
fun TabButton(
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val backgroundColor = if (selected) SelectedTabColor else NotSelectedTabColor

    val baseModifier = modifier
        .height(tabButtonCornerHeight)
        .background(color = backgroundColor, shape = RoundedCornerShape(tabButtonCornerRadius))
        .shadow(
            elevation = shadowElevation,
            spotColor = ShadowColor,
            ambientColor = ShadowColor,
            shape = RoundedCornerShape(tabButtonCornerRadius)
        )

    Box(
        modifier = baseModifier
            .clip(RoundedCornerShape(tabButtonCornerRadius))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TabButtonTextStyle
        )
    }
}