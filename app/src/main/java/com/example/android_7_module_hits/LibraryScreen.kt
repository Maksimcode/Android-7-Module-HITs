package com.example.android_7_module_hits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android_7_module_hits.ui.theme.deepblue
import com.example.android_7_module_hits.ui.theme.lightblue


@Composable
fun MainContent(
    navController: NavController
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        Buttons()
        Spacer(modifier = Modifier.height(16.dp))
        GreetProjectBlocks(navController)
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

    ){
        Text (
            text= "Library",
            fontSize = 48.sp,
        )

        Text (
            text = "All projects here!",
            fontSize = 24.sp,
        )
    }
}

@Composable
fun Buttons() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = {
            }
        ) {
            Text("My projects")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = {
            }
        ) {
            Text("Examples")
        }
    }
}

@Composable
fun GreetProjectBlocks(navController: NavController){
    //val unknownProjectBlock = ProjectBlock("Unknown", "1 Jan, 1970")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items((1..5).toList()) { index ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProjectBlock("Unknown", "1 Jan, 1970", navController)
                ProjectBlock("Unknown", "1 Jan, 1970", navController)
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
//    onClick: () -> Unit = {}
){
    Card(
        modifier = Modifier
            .size(160.dp)
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                color = contentColor,
                fontSize = 16.sp
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