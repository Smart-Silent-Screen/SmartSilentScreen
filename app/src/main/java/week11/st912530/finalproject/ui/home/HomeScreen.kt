package week11.st912530.finalproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavHostController, vm: AuthViewModel = viewModel()) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Welcome!", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                vm.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }
    }
}