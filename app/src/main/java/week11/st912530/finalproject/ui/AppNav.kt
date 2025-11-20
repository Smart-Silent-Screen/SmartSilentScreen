package week11.st912530.finalproject.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st912530.finalproject.ui.auth.LoginScreen
import week11.st912530.finalproject.ui.auth.SignupScreen
import week11.st912530.finalproject.ui.home.EventLogsScreen
import week11.st912530.finalproject.ui.home.HomeScreen
import week11.st912530.finalproject.viewmodel.AuthViewModel
import week11.st912530.finalproject.viewmodel.LogsViewModel

@Composable
fun AppNav(navController: NavHostController) {

    val authVm: AuthViewModel = viewModel()
    val logsVm: LogsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, authVm) }
        composable("signup") { SignupScreen(navController, authVm) }
        composable("home") { HomeScreen(navController, authVm) }
        composable("logs") { EventLogsScreen(navController, logsVm) }
    }
}