package week11.st912530.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import week11.st912530.finalproject.ui.AppNav
import week11.st912530.finalproject.ui.theme.SmartSilentScreenTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartSilentScreenTheme {
                val navController = rememberNavController()
                AppNav(
                    navController = navController,
                    activity = this
                )
            }
        }
    }
}