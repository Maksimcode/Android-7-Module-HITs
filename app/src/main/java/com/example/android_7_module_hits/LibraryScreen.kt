package com.example.android_7_module_hits

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android_7_module_hits.ui.theme.deepblue
import com.example.android_7_module_hits.ui.theme.lightblue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.ui.theme.LibrarySubTitle
import com.example.android_7_module_hits.ui.theme.LibraryTitle


@Composable
fun MainContent(
    navController: NavController,
    projectViewModel: ProjectViewModel = viewModel()
){
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("My projects", "Examples")
    val myProjects = projectViewModel.myProjects

    SideEffect {
        Log.d("LibraryScreen", "Сохранённых проектов: ${myProjects.size}")
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
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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

            when (selectedTabIndex) {
                0 -> {
                    if (myProjects.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "It's empty now :(")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(myProjects) { project ->
                                ProjectCard(projectPreview = project)
                            }
                        }
                    }
                }
                1 -> {
                    GreetProjectBlocks(navController)
                }
            }
        }
        if (selectedTabIndex == 0) {
            RightBottomCircleButton(navController)
        }
    }
}

@Composable
fun TabButton(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    // Общие параметры размеров и фона
    val baseModifier = Modifier
        .width(180.dp)
        .height(52.dp)
        .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(24.dp))

    val modifier = if (selected) {
        // Для активной вкладки: сначала shadow, потом базовые модификаторы
        Modifier
            .shadow(
                elevation = 25.dp,
                spotColor = Color(0x40E2E2E2),
                ambientColor = Color(0x40E2E2E2)
            )
            .then(baseModifier)
    } else {
        // Для неактивной вкладки: сначала базовые модификаторы, затем offset и shadow
        baseModifier
            .shadow(
                elevation = 25.dp,
                spotColor = Color(0x40E2E2E2),
                ambientColor = Color(0x40E2E2E2)
            )
            .background(color = Color(0xFFCFD8F9), shape = RoundedCornerShape(24.dp))
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
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

    ){
        Text (
            color = LibraryTitle,
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
fun ProjectCard(projectPreview: ProjectPreview) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Проект сохранён:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = projectPreview.saveDate,
                style = MaterialTheme.typography.bodyMedium
            )
        }
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
    modifier: Modifier = Modifier,
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(lightblue, deepblue),
        start = Offset(0f, 0f),
        end = Offset(300f, 600f)
    ),
    contentColor: Color = Color.White,
){
    Card(
        modifier = Modifier
            .size(176.dp)
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
            // Для чего он вообще? Spacer(modifier = Modifier.weight(1f))
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
fun RightBottomCircleButton(navController: NavController) {
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
                    color = deepblue,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable {
                    navController.navigate(route = Screen.Workspace.route)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Workspace",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun LibraryScreenPreview(){
    MainContent(
        navController = rememberNavController()
    )
}
//  TODO: classes for headings, subheadings...
//  another file for text-classes
//  eliminate the use of spacer (replace with indents inside the component parameters)
//  class for project