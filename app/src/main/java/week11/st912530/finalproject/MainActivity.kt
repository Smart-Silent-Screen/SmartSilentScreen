package week11.st912530.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import week11.st912530.finalproject.ui.AppNav
import week11.st912530.finalproject.ui.theme.SmartSilentScreenTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.orientationService.deviceController.setActivity(this)

        setContent {
            SmartSilentScreenTheme {
                val navController = rememberNavController()
                AppNav(navController, mainViewModel.orientationService)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.orientationService.start()
        mainViewModel.orientationService.deviceController.setActivity(this)
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.orientationService.stop()
    }
}
