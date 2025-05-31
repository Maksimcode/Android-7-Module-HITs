package com.example.android_7_module_hits.ui.libraryFuns

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.saving.utils.SaveProject
import com.example.android_7_module_hits.saving.utils.ensureJsonExtension
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.cardCornerRadius
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.cardElevation
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.cardPadding
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.gradientEndOffset
import com.example.android_7_module_hits.ui.theme.LibraryFunctionsDimens.gradientStartOffset
import com.example.android_7_module_hits.ui.theme.ProjectSaveBlockContentColor
import com.example.android_7_module_hits.ui.theme.SaveBlockDateStyle
import com.example.android_7_module_hits.ui.theme.SaveBlockTitleStyle
import com.example.android_7_module_hits.ui.theme.deepBlue
import com.example.android_7_module_hits.ui.theme.lightBlue

@Composable
fun ProjectSaveBlock(
    saveProject: SaveProject,
    navController: NavController,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(lightBlue, deepBlue),
        start = gradientStartOffset,
        end = gradientEndOffset
    ),
) {
    var showDeleteIcon by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(cardCornerRadius))
            .background(brush = gradientBrush)
            .combinedClickable(
                onClick = {
                    if (showDeleteIcon) {
                        showDeleteIcon = false
                    } else {
                        navController.navigate("${Screen.Workspace.route}/${ensureJsonExtension(saveProject.fileName)}")
                    }
                },
                onLongClick = { showDeleteIcon = true }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
                    .padding(cardPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                val displayName = if (saveProject.fileName.length > 10) {
                    saveProject.fileName.take(10) + stringResource(id = R.string.three_points_to_saving)
                } else {
                    saveProject.fileName
                }
                Text(
                    text = displayName,
                    style = SaveBlockTitleStyle,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = saveProject.saveDate,
                    style = SaveBlockDateStyle,
                )
            }
            if (showDeleteIcon) {
                IconButton(
                    onClick = {
                        onDelete()
                        showDeleteIcon = false
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_save_block_description),
                        tint = ProjectSaveBlockContentColor
                    )
                }
            }
        }
    }
}
