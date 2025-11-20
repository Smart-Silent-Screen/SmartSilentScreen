package week11.st912530.finalproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.ui.components.*
import week11.st912530.finalproject.viewmodel.LogsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventLogsScreen(navController: NavHostController, vm: LogsViewModel) {

    ScreenContainer {
        ScreenHeader("Event Logs")
        CommonSpacing.Medium()

        vm.errorMessage?.let {
            ErrorMessage(it)
            CommonSpacing.Small()
        }

        if (vm.events.isEmpty()) {
            Text("No logs yet.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(vm.events) { log ->
                    EventLogCard(
                        event = log.event,
                        timestamp = log.timestamp,
                        duration = log.duration,
                        sessionId = log.sessionId
                    )
                }
            }
        }
    }
}

@Composable
fun EventLogCard(
    event: String,
    timestamp: Long,
    duration: Long?,
    sessionId: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = event,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            
            // Format timestamp to readable date/time
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
            Text(
                text = "Time: ${dateFormat.format(Date(timestamp))}",
                style = MaterialTheme.typography.bodySmall
            )
            
            // Show duration if available
            duration?.let {
                Spacer(Modifier.height(2.dp))
                val durationSeconds = it / 1000
                val minutes = durationSeconds / 60
                val seconds = durationSeconds % 60
                Text(
                    text = "Duration: ${minutes}m ${seconds}s",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Show session ID if available
            sessionId?.let {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Session: ${it.take(8)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}