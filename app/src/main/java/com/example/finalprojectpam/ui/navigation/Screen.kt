package com.example.finalprojectpam.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Score : Screen("score")
    object Story : Screen("story/{storyFileName}") {
        fun createRoute(storyFileName: String) = "story/$storyFileName"
    }
}
