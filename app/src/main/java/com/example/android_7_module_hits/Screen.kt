package com.example.android_7_module_hits

sealed class Screen(val route: String) {
    object Library: Screen(route ="library_screen")
    object Workspace: Screen(route ="Workspace_screen")
}