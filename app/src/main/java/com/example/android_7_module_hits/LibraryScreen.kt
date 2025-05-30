package com.example.android_7_module_hits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.saving.utils.SaveProject
import com.example.android_7_module_hits.saving.utils.ensureJsonExtension
import com.example.android_7_module_hits.saving.utils.getSaveProjects
import com.example.android_7_module_hits.ui.libraryFuns.EmptyStateContent
import com.example.android_7_module_hits.ui.libraryFuns.Header
import com.example.android_7_module_hits.ui.libraryFuns.ProjectSaveBlock
import com.example.android_7_module_hits.ui.libraryFuns.RightBottomCircleButton
import com.example.android_7_module_hits.ui.libraryFuns.TabButton
import com.example.android_7_module_hits.ui.theme.MainContentDimens.contentPadding
import com.example.android_7_module_hits.ui.theme.MainContentDimens.contentVerticalSpacerHeight
import com.example.android_7_module_hits.ui.theme.MainContentDimens.headerBottomSpacerHeight
import com.example.android_7_module_hits.ui.theme.MainContentDimens.headerSpacerHeight
import com.example.android_7_module_hits.ui.theme.MainContentDimens.lazyColumnVerticalSpacing
import com.example.android_7_module_hits.ui.theme.MainContentDimens.rowProjectsSpacing
import com.example.android_7_module_hits.ui.theme.MainContentDimens.tabRowSpacing
import com.example.android_7_module_hits.viewModel.BlockViewModel


@Composable
fun MainContent(
    navController: NavController,
){

    val viewModel: BlockViewModel = viewModel<BlockViewModel>()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        stringResource(id = R.string.first_tab_button_text),
        stringResource(id = R.string.second_tab_button_text)
    )
    val context = LocalContext.current

    var saveProjects by remember { mutableStateOf(emptyList<SaveProject>()) }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 0) {
            saveProjects = getSaveProjects(context)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Spacer(modifier = Modifier.height(headerSpacerHeight))
            Header()
            Spacer(modifier = Modifier.height(headerBottomSpacerHeight))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(tabRowSpacing, Alignment.CenterHorizontally)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    TabButton(
                        selected = selectedTabIndex == index,
                        text = title,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
            Spacer(modifier = Modifier.height(contentVerticalSpacerHeight))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTabIndex) {
                0 -> {
                    if (saveProjects.isEmpty()) {
                        EmptyStateContent()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(lazyColumnVerticalSpacing),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(saveProjects.chunked(2)) { rowProjects ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(rowProjectsSpacing)
                                ) {
                                    rowProjects.forEach { project ->
                                        ProjectSaveBlock(
                                            saveProject = project,
                                            navController = navController,
                                            onDelete = {
                                                viewModel.deleteSaveFile(ensureJsonExtension(project.fileName)) {
                                                    saveProjects = getSaveProjects(context)
                                                }
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                        )
                                    }
                                    if (rowProjects.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    EmptyStateContent()
                }
            }
        }
    }
        if (selectedTabIndex == 0) {
            RightBottomCircleButton(
                onClick = {
                    navController.navigate("${Screen.Workspace.route}/Enter project name")
                }
            )
        }
    }
}

