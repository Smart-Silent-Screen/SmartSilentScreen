package week11.st912530.finalproject.ui.sensor

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.data.model.OrientationState
import week11.st912530.finalproject.ui.components.*
import week11.st912530.finalproject.utils.PermissionHelper
import week11.st912530.finalproject.viewmodel.SensorViewModel

/**
 * Screen for controlling face-down detection and viewing current status.
 */
@Composable
fun SensorScreen(navController: NavHostController, vm: SensorViewModel) {
    
    val context = LocalContext.current
    
    // Refresh permissions when screen becomes visible
    androidx.compose.runtime.DisposableEffect(Unit) {
        vm.refreshPermissions()
        onDispose { }
    }

    ScreenContainer {
        ScreenHeader("Face Down Detection")
        CommonSpacing.Medium()

        // Error message if any
        vm.errorMessage?.let {
            ErrorMessage(it)
            CommonSpacing.Small()
            Button(onClick = { vm.clearError() }) {
                Text("Dismiss")
            }
            CommonSpacing.Medium()
        }
        
        // Permission status
        if (!vm.hasRequiredPermissions && vm.missingPermissions.isNotEmpty()) {
            PermissionCard(
                missingPermissions = vm.missingPermissions,
                onRequestPermission = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionHelper.requestDoNotDisturbPermission(context)
                    }
                }
            )
            CommonSpacing.Medium()
        }

        // Sensor availability status
        if (!vm.isSensorAvailable) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Accelerometer Not Available",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "This device doesn't have an accelerometer sensor. Face-down detection is not possible.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else {
            // Current orientation status
            OrientationStatusCard(vm.currentOrientation)

            CommonSpacing.Medium()

            // Current mode card
            ModeStatusCard(
                mode = vm.currentMode.displayName(),
                isSilent = vm.currentMode.isSilent()
            )

            CommonSpacing.Medium()

            // Session info if face down
            vm.currentSession?.let { session ->
                SessionInfoCard(
                    sessionId = session.sessionId,
                    startTime = session.startTime
                )
                CommonSpacing.Medium()
            }

            // Toggle monitoring button
            Button(
                onClick = { vm.toggleMonitoring() },
                modifier = Modifier.fillMaxWidth(),
                colors = if (vm.isFaceDownModeEnabled) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(
                    if (vm.isFaceDownModeEnabled) {
                        "Stop Monitoring"
                    } else {
                        "Start Monitoring"
                    }
                )
            }

            CommonSpacing.Small()

            // Status text
            Text(
                text = if (vm.isFaceDownModeEnabled) {
                    "Monitoring active - phone orientation is being tracked"
                } else {
                    "Monitoring stopped - tap button to start"
                },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun OrientationStatusCard(orientation: OrientationState) {
    val (icon, text, color) = when (orientation) {
        is OrientationState.FaceDown -> Triple("📱", "Face Down", Color(0xFFFF6B6B))
        is OrientationState.FaceUp -> Triple("📱", "Face Up", Color(0xFF4ECDC4))
        is OrientationState.Unknown -> Triple("❓", "Unknown", Color.Gray)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Z-axis: ${"%.2f".format(
                    when (orientation) {
                        is OrientationState.FaceDown -> orientation.zValue
                        is OrientationState.FaceUp -> orientation.zValue
                        is OrientationState.Unknown -> orientation.zValue
                    }
                )} m/s²",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ModeStatusCard(mode: String, isSilent: Boolean) {
    val (icon, color) = if (isSilent) {
        "🔇" to Color(0xFFFF6B6B)
    } else {
        "🔔" to Color(0xFF4ECDC4)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "Current Mode",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = mode,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun SessionInfoCard(sessionId: String, startTime: Long) {
    val durationSeconds = (System.currentTimeMillis() - startTime) / 1000
    val minutes = durationSeconds / 60
    val seconds = durationSeconds % 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Active Session",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Duration: ${minutes}m ${seconds}s",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Session ID: ${sessionId.take(8)}...",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

