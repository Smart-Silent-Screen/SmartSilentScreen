import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFF6F7FF),
                        Color(0xFFEFF2FF)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = if (first.isNotBlank()) "Welcome, $first!" else "Welcome!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222)
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
                            val intent =
                                Intent(context, OrientationMonitorService::class.java)
                            ContextCompat.startForegroundService(context, intent)
                        } else {
                            orientationService.disableAutomation()
                            val intent =
                                Intent(context, OrientationMonitorService::class.java)
                            context.stopService(intent)
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                FeatureCard("Event Logs") {
                    navController.navigate("logs")
                }

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = {
                        vm.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun OrientationStatusCard(state: OrientationState) {
    val (status, color, subtitle) = when (state) {
        is OrientationState.FaceUp ->
            Triple("Face Up", Color(0xFF4CAF50), "Screen is facing upwards")
        is OrientationState.FaceDown ->
            Triple("Face Down", Color(0xFFFF5722), "Screen is facing down")
        is OrientationState.Unknown ->
            Triple("Unknown", Color(0xFF9E9E9E), "Orientation not detected yet")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ScreenRotation,
                    contentDescription = "Orientation",
                    tint = color
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "Phone Orientation",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    status,
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DeviceModeCard(mode: DeviceMode) {
    val (status, color, icon) = when (mode) {
        DeviceMode.NORMAL -> Triple(
            "Normal Mode",
            Color(0xFF2196F3),
            Icons.Filled.Notifications
        )

        DeviceMode.SILENT -> Triple(
            "Silent Mode Active",
            Color(0xFFFF9800),
            Icons.Filled.NotificationsOff
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Device status",
                    tint = color
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "Device Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    status,
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ToggleControlsCard(isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    val labelColor = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF9E9E9E)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            labelColor.copy(alpha = 0.12f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhoneAndroid,
                        contentDescription = "Automation",
                        tint = labelColor
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Automation Control",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (isEnabled) "Active" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = labelColor
                    )
                }
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
            .animateContentSize()
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFDEE2F7),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Article,
                        contentDescription = title,
                        tint = Color(0xFF3949AB)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            if (onClick != null) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Go to $title",
                    tint = Color(0xFF9E9E9E)
                )
            }
        }
    }
}