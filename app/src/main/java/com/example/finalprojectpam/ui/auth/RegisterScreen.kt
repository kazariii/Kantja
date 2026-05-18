package com.example.finalprojectpam.ui.auth

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.finalprojectpam.ui.navigation.Screen

// Register sudah digabung ke LoginScreen (tab "Daftar").
// Route ini hanya sebagai fallback redirect.
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    LaunchedEffect(Unit) {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Register.route) { inclusive = true }
        }
    }
}
