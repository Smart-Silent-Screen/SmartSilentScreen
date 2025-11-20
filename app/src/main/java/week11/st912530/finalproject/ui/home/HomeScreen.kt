package week11.st912530.finalproject.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavHostController, vm: AuthViewModel) {

    val first = vm.userProfile?.get("firstName")?.toString() ?: ""

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {

            Text(
                text = if (first.isNotBlank()) "Welcome, $first!" else "Welcome!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            FeatureCard("Face Up / Face Down Status")
            FeatureCard("Silent Mode Automation")
            FeatureCard("Event Logs") {
                navController.navigate("logs")
            }
            FeatureCard("Toggle Controls")

            Spacer(Modifier.height(30.dp))

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

@Composable
fun FeatureCard(title: String, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}