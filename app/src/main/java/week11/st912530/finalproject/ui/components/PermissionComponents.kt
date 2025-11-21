package week11.st912530.finalproject.ui.components

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import week11.st912530.finalproject.utils.PermissionHelper

/**
 * Card displaying permission requirements and status.
 */
@Composable
fun PermissionCard(
    missingPermissions: List<String>,
    onRequestPermission: () -> Unit
) {
    if (missingPermissions.isEmpty()) return
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "⚠️ Permission Required",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            CommonSpacing.Small()
            
            missingPermissions.forEach { permission ->
                Text(
                    text = "• ${PermissionHelper.getPermissionDisplayName(permission)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "  ${PermissionHelper.getPermissionExplanation(permission)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(Modifier.height(4.dp))
            }
            
            CommonSpacing.Small()
            
            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Grant Permission")
            }
        }
    }
}

/**
 * Compact permission warning banner.
 */
@Composable
fun PermissionBanner(
    hasPermission: Boolean,
    permissionName: String,
    onRequestPermission: () -> Unit
) {
    if (hasPermission) return
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "⚠️ $permissionName Required",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            TextButton(onClick = onRequestPermission) {
                Text(
                    "Grant",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

