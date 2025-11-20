package week11.st912530.finalproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.viewmodel.LogsViewModel

@Composable
fun EventLogsScreen(navController: NavHostController, vm: LogsViewModel) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Event Logs", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            vm.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            if (vm.events.isEmpty()) {
                Text("No logs yet.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(vm.events) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = log.event, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Time: ${log.timestamp}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}