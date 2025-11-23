package week11.st912530.finalproject.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val currentUser = authVm.currentUser
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Use a fixed start destination and reactively navigate when auth changes
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, authVm) }
        composable("signup") { SignupScreen(navController, authVm) }
        composable("home") { HomeScreen(navController, authVm, orientationService) }
        composable("logs") { EventLogsScreen(navController, logsVm) }
    }
}