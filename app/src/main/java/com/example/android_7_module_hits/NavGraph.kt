package com.example.android_7_module_hits

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    val activity = LocalActivity.current
    val projectViewModel: ProjectViewModel = viewModel(
        viewModelStoreOwner = activity as ComponentActivity
    )
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ){
        composable(
            route = Screen.Library.route
        ){
            MainContent(navController = navController,
                projectViewModel = projectViewModel)
        }
        composable(
            route = Screen.Workspace.route
        ){
            MainScreen(navController = navController,
                projectViewModel = projectViewModel)
        }
    }
}