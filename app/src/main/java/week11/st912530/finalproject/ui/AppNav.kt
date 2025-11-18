package week11.st912530.finalproject.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st912530.finalproject.ui.auth.LoginScreen
import week11.st912530.finalproject.ui.auth.SignupScreen
import week11.st912530.finalproject.ui.home.HomeScreen

@Composable
fun AppNav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
    }
}