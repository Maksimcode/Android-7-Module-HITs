package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.interpreter.InterpreterLauncher
import com.example.android_7_module_hits.ui.theme.FolderButtonMain
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonMain
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonMain
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonMain
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.buttonCornerRadius
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.buttonShadowElevation
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.buttonSize
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.iconSize
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.rowBottomPadding
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.rowButtonSpacing
import com.example.android_7_module_hits.ui.theme.buttonCircleButtonsShadow
import com.example.android_7_module_hits.utils.logAllBlocks

@Composable
fun BottomCircleButtons(
    allBlocks: List<Block>,
    onConsoleClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val buttonColors = listOf(
        FolderButtonMain,
        StopButtonSub,
        SettingsButtonMain,
        RunButtonMain
    )
    val iconList: List<ImageVector> = listOf(
        Icons.Filled.Folder,
        Icons.Filled.CheckBoxOutlineBlank,
        Icons.Filled.BugReport,
        Icons.Filled.PlayArrow
    )
    val iconTints = listOf(
        FolderButtonSub,
        StopButtonMain,
        SettingsButtonSub,
        RunButtonSub
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = rowBottomPadding)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(rowButtonSpacing, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Top,
        ) {
            buttonColors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = buttonShadowElevation,
                            spotColor = buttonCircleButtonsShadow,
                            ambientColor = buttonCircleButtonsShadow,
                            shape = RoundedCornerShape(buttonCornerRadius)
                        )
                        .size(buttonSize)
                        .background(
                            color = color,
                            shape = RoundedCornerShape(buttonCornerRadius)
                        )
                        .clickable {
                            when (index) {
                                0 -> onSaveClick()
                                1 -> {}
                                2 -> {}
                                3 -> {
                                    logAllBlocks(allBlocks)
                                    InterpreterLauncher.launchInterpreter(
                                        lifecycleScope = scope,
                                        blocks = allBlocks
                                    ) {
                                        onConsoleClick()
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconList[index],
                        contentDescription = "Icon ${index + 1}",
                        tint = iconTints[index],
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}