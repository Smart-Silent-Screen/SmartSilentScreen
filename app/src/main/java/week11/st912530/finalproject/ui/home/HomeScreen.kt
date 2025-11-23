package week11.st912530.finalproject.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import week11.st912530.finalproject.device.DeviceMode
import week11.st912530.finalproject.sensor.OrientationService
import week11.st912530.finalproject.sensor.OrientationState
import week11.st912530.finalproject.service.OrientationMonitorService
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavHostController, 
    vm: AuthViewModel,
    orientationService: OrientationService
) {
    val first = vm.userProfile?.get("firstName")?.toString() ?: ""
    val orientationState by orientationService.orientationState.collectAsState()
    val deviceMode by orientationService.deviceController.currentMode.collectAsState()
    val isAutomationEnabled by orientationService.isAutomationEnabled.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = if (first.isNotBlank()) "Welcome, $first!" else "Welcome!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(24.dp))

            OrientationStatusCard(orientationState)

            Spacer(Modifier.height(12.dp))

            DeviceModeCard(deviceMode)

            Spacer(Modifier.height(20.dp))
            
            ToggleControlsCard(
                isEnabled = isAutomationEnabled,
                onToggle = { enabled ->
                    if (enabled) {
                        orientationService.enableAutomation()
                        val intent = Intent(context, OrientationMonitorService::class.java)
                        ContextCompat.startForegroundService(context, intent)
                    } else {
                        orientationService.disableAutomation()
                        val intent = Intent(context, OrientationMonitorService::class.java)
                        context.stopService(intent)
                    }
                }
            )
            
            Spacer(Modifier.height(12.dp))
            
            FeatureCard("Event Logs") {
                navController.navigate("logs")
            }

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
fun OrientationStatusCard(state: OrientationState) {
    val (status, color) = when (state) {
        is OrientationState.FaceUp -> "Face Up" to Color(0xFF4CAF50)
        is OrientationState.FaceDown -> "Face Down" to Color(0xFFFF5722)
        is OrientationState.Unknown -> "Unknown" to Color(0xFF9E9E9E)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, RoundedCornerShape(6.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Phone Orientation",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    status,
                    style = MaterialTheme.typography.titleLarge,
                    color = color
                )
            }
        }
    }
}

@Composable
fun DeviceModeCard(mode: DeviceMode) {
    val (status, color) = when (mode) {
        DeviceMode.NORMAL -> "Normal Mode" to Color(0xFF2196F3)
        DeviceMode.SILENT -> "Silent Mode Active" to Color(0xFFFF9800)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, RoundedCornerShape(6.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Device Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    status,
                    style = MaterialTheme.typography.titleLarge,
                    color = color
                )
            }
        }
    }
}

@Composable
fun ToggleControlsCard(isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Automation Control",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isEnabled) "Active" else "Disabled",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
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