package week11.st912530.finalproject.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st912530.finalproject.sensor.OrientationService
import week11.st912530.finalproject.ui.auth.LoginScreen
import week11.st912530.finalproject.ui.auth.SignupScreen
import week11.st912530.finalproject.ui.home.EventLogsScreen
import week11.st912530.finalproject.ui.home.HomeScreen
import week11.st912530.finalproject.viewmodel.AuthViewModel
import week11.st912530.finalproject.viewmodel.LogsViewModel

@Composable
fun AppNav(navController: NavHostController, orientationService: OrientationService) {

    val authVm: AuthViewModel = viewModel()
    val logsVm: LogsViewModel = viewModel()

    val startDestination = if (authVm.currentUser != null) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController, authVm) }
        composable("signup") { SignupScreen(navController, authVm) }
        composable("home") { HomeScreen(navController, authVm, orientationService) }
        composable("logs") { EventLogsScreen(navController, logsVm) }
    }
}