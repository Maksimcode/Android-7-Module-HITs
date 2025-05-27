package com.example.android_7_module_hits.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            route = "${Screen.Workspace.route}/{fileName}",
            arguments = listOf(
                navArgument("fileName"){
                    type = NavType.StringType
                }
            )
        ){
            backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName") ?: ""
            MainScreen(
                navController = navController,
                fileName = fileName)
        }
    }
}