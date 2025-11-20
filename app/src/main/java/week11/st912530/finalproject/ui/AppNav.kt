package week11.st912530.finalproject.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository
import week11.st912530.finalproject.sensor.AccelerometerManager
import week11.st912530.finalproject.ui.auth.LoginScreen
import week11.st912530.finalproject.ui.auth.SignupScreen
import week11.st912530.finalproject.ui.home.EventLogsScreen
import week11.st912530.finalproject.ui.home.HomeScreen
import week11.st912530.finalproject.ui.sensor.SensorScreen
import week11.st912530.finalproject.viewmodel.AuthViewModel
import week11.st912530.finalproject.viewmodel.LogsViewModel
import week11.st912530.finalproject.viewmodel.SensorViewModel

@Composable
fun AppNav(navController: NavHostController) {

    val context = LocalContext.current
    
    // ViewModels with default implementations
    val authVm: AuthViewModel = viewModel()
    val logsVm: LogsViewModel = viewModel()
    
    // SensorViewModel with manual dependency injection
    // We need context to create AccelerometerManager
    val sensorVm: SensorViewModel = viewModel(
        factory = SensorViewModelFactory(
            context = context
        )
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, authVm) }
        composable("signup") { SignupScreen(navController, authVm) }
        composable("home") { HomeScreen(navController, authVm) }
        composable("logs") { EventLogsScreen(navController, logsVm) }
        composable("sensor") { SensorScreen(navController, sensorVm) }
    }
}

/**
 * Factory for creating SensorViewModel with dependencies.
 * Following Dependency Inversion Principle.
 */
class SensorViewModelFactory(
    private val context: Context
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            return SensorViewModel(
                sensorManager = AccelerometerManager(context),
                firestoreRepository = FirestoreRepository(),
                authRepository = AuthRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}