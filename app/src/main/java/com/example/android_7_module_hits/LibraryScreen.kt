package com.example.android_7_module_hits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavController
import com.example.android_7_module_hits.ui.theme.deepBlue
import com.example.android_7_module_hits.ui.theme.lightBlue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.saving.utils.SaveProject
import com.example.android_7_module_hits.saving.utils.ensureJsonExtension
import com.example.android_7_module_hits.saving.utils.getSaveProjects
import com.example.android_7_module_hits.ui.theme.LibrarySubTitle
import com.example.android_7_module_hits.ui.theme.MainTextColor
import com.example.android_7_module_hits.ui.theme.NotSelectedTabColor
import com.example.android_7_module_hits.ui.theme.SelectedTabColor
import com.example.android_7_module_hits.ui.theme.ShadowColor
import com.example.android_7_module_hits.viewModel.BlockViewModel


@Composable
fun MainContent(
    navController: NavController,
){

    val viewModel: BlockViewModel = viewModel<BlockViewModel>()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("My projects", "Examples")
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
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Header()
            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    TabButton(
                        selected = selectedTabIndex == index,
                        text = title,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

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
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(saveProjects.chunked(2)) { rowProjects ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    rowProjects.forEach { project ->
                                        ProjectSaveBlock(
                                            saveProject = project,
                                            navController = navController,
                                            onDelete = {
                                                viewModel.deleteSaveFile(ensureJsonExtension(project.fileName)) {
                                                    saveProjects = getSaveProjects(context)
                                                }
                                            }
                                        )
                                    }
                                    if (rowProjects.size < 2) {
                                        Spacer(modifier = Modifier.size(168.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    GreetProjectBlocks(navController)
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

@Composable
fun EmptyStateContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty),
            contentDescription = "Empty illustration",
            modifier = Modifier.size(244.dp)
        )
        Text(
            text = "It's empty now :(",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                color = MainTextColor
            )
        )
    }
}

@Composable
fun TabButton(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val baseModifier = Modifier
        .width(168.dp)
        .height(52.dp)
        .background(color = SelectedTabColor, shape = RoundedCornerShape(24.dp))

    val modifier = if (selected) {
        Modifier
            .shadow(
                elevation = 25.dp,
                spotColor = ShadowColor,
                ambientColor = ShadowColor
            )
            .then(baseModifier)
    } else {
        baseModifier
            .shadow(
                elevation = 25.dp,
                spotColor = ShadowColor,
                ambientColor = ShadowColor
            )
            .background(color = NotSelectedTabColor, shape = RoundedCornerShape(24.dp))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        val baseStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
        Text(
            text = text,
            style = if (selected) {
                baseStyle.copy(fontWeight = FontWeight(700))
            } else {
                baseStyle.copy(fontWeight = FontWeight.Normal)
            }
        )


    }
}

@Composable
fun ProjectSaveBlock(
    saveProject: SaveProject,
    navController: NavController,
    onDelete: () -> Unit,
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(lightBlue, deepBlue),
        start = Offset(0f, 0f),
        end = Offset(300f, 600f)
    ),
    contentColor: Color = Color.White
) {
    Card(
        modifier = Modifier
            .size(168.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradientBrush)
            .clickable {
                navController.navigate("${Screen.Workspace.route}/${ensureJsonExtension(saveProject.fileName)}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                val displayName = if (saveProject.fileName.length > 10) {
                    saveProject.fileName.take(10) + "..."
                } else {
                    saveProject.fileName
                }
                Text(
                    text = displayName,
                    color = contentColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = saveProject.saveDate,
                    color = contentColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(300)

                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Save",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

    ){
        Text (
            color = MainTextColor,
            text= "Library",
            fontSize = 48.sp,
        )

        Text (
            color = LibrarySubTitle,
            text = "All projects here!",
            fontSize = 24.sp,
        )
    }
}


@Composable
fun GreetProjectBlocks(navController: NavController){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items((1..5).toList()) { index ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ProjectBlock("Unknown", "May 1,\n2025", navController)
                ProjectBlock("Unknown", "May 1,\n2025", navController)
            }
        }
    }
}

@Composable
fun ProjectBlock(
    title: String,
    date: String,
    navController: NavController,
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(lightBlue, deepBlue),
        start = Offset(0f, 0f),
        end = Offset(300f, 600f)
    ),
    contentColor: Color = Color.White,
){
    Card(
        modifier = Modifier
            .size(168.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradientBrush)
            .clickable {
                navController.navigate(route = Screen.Workspace.route)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = contentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = date,
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight(300)
            )
        }
    }
}

@Composable
fun RightBottomCircleButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(
                    elevation = 25.dp,
                    spotColor = Color(0x40E2E2E2),
                    ambientColor = Color(0x40E2E2E2),
                    shape = RoundedCornerShape(32.dp)
                )
                .size(56.dp)
                .background(
                    color = deepBlue,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Create new workspace",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}