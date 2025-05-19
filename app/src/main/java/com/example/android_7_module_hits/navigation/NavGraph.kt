package com.example.android_7_module_hits.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_7_module_hits.MainContent
import com.example.android_7_module_hits.MainScreen


@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ){
        composable(
            route = Screen.Library.route
        ){
            MainContent(navController = navController)
        }
        composable(
            route = Screen.Workspace.route
        ){
            MainScreen(navController = navController)
        }
    }
}