package week11.st912530.finalproject.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Reusable screen container with Scaffold and consistent padding.
 * Following DRY Principle - define once, use everywhere.
 */
@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

/**
 * Reusable error message display
 */
@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

/**
 * Reusable info message display
 */
@Composable
fun InfoMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

/**
 * Reusable screen header
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

/**
 * Reusable vertical spacer with common sizes
 */
object CommonSpacing {
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 30.dp
    
    @Composable
    fun Small() = Spacer(Modifier.height(small))
    
    @Composable
    fun Medium() = Spacer(Modifier.height(medium))
    
    @Composable
    fun Large() = Spacer(Modifier.height(large))
    
    @Composable
    fun ExtraLarge() = Spacer(Modifier.height(extraLarge))
}

