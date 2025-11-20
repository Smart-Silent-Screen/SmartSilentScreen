package week11.st912530.finalproject.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.ui.components.*
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavHostController, vm: AuthViewModel) {

    val first = vm.userProfile?.get("firstName")?.toString() ?: ""

    ScreenContainer {
        ScreenHeader(
            title = if (first.isNotBlank()) "Welcome, $first!" else "Welcome!"
        )

        CommonSpacing.Large()

        FeatureCard("Face Down Detection & Control") {
            navController.navigate("sensor")
        }
        FeatureCard("Event Logs") {
            navController.navigate("logs")
        }

        CommonSpacing.ExtraLarge()

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