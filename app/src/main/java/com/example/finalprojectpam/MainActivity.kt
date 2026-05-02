package com.example.finalprojectpam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalprojectpam.data.local.preferences.AuthPreferences
import com.example.finalprojectpam.ui.auth.AuthViewModel
import com.example.finalprojectpam.ui.auth.LoginScreen
import com.example.finalprojectpam.ui.auth.RegisterScreen
import com.example.finalprojectpam.ui.dashboard.ParentDashboardScreen
import com.example.finalprojectpam.ui.home.HomeScreen
import com.example.finalprojectpam.ui.navigation.Screen
import com.example.finalprojectpam.ui.profile.ProfileScreen
import com.example.finalprojectpam.ui.score.ScoreScreen
import com.example.finalprojectpam.ui.story.StoryScreen
import com.example.finalprojectpam.ui.theme.KancaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KancaTheme {
                KancaApp()
            }
        }
    }
}

@Composable
fun KancaApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination = remember {
        val prefs = AuthPreferences(context)
        when {
            !prefs.isLoggedIn() -> Screen.Login.route
            prefs.getRole() == AuthPreferences.ROLE_PARENT -> Screen.ParentDashboard.route
            else -> Screen.ChildDashboard.route
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.ChildDashboard.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.ParentDashboard.route) {
            ParentDashboardScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Score.route) {
            ScoreScreen(navController = navController)
        }
        composable(
            route = Screen.Story.route,
            arguments = listOf(navArgument("storyFileName") { type = NavType.StringType })
        ) { backStackEntry ->
            val storyFileName = backStackEntry.arguments?.getString("storyFileName") ?: ""
            StoryScreen(navController = navController, storyFileName = storyFileName)
        }
    }
}
