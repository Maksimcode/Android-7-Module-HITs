package com.example.android_7_module_hits.ui.libraryFuns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.ui.theme.BottomCircleButtonBackgroundColor
import com.example.android_7_module_hits.ui.theme.BottomCircleButtonTintColor
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.rightBottomButtonCornerRadius
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.rightBottomButtonPadding
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.rightBottomButtonSize
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.rightBottomIconSize
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.shadowElevation
import com.example.android_7_module_hits.ui.theme.ShadowColor

@Composable
fun RightBottomCircleButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(rightBottomButtonPadding)
                .shadow(
                    elevation = shadowElevation,
                    spotColor = ShadowColor,
                    ambientColor = ShadowColor,
                    shape = RoundedCornerShape(rightBottomButtonCornerRadius)
                )
                .size(rightBottomButtonSize)
                .background(
                    color = BottomCircleButtonBackgroundColor,
                    shape = RoundedCornerShape(rightBottomButtonCornerRadius)
                )
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.bottom_circle_button_description),
                tint = BottomCircleButtonTintColor,
                modifier = Modifier.size(rightBottomIconSize)
            )
        }
    }
}