package week11.st912530.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import week11.st912530.finalproject.sensor.OrientationService
import week11.st912530.finalproject.ui.AppNav
import week11.st912530.finalproject.ui.theme.SmartSilentScreenTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var orientationService: OrientationService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        orientationService = OrientationService(this)
        orientationService.deviceController.setActivity(this)
        
        setContent {
            SmartSilentScreenTheme {
                val navController = rememberNavController()
                AppNav(navController, orientationService)
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        orientationService.start()
        orientationService.deviceController.setActivity(this)
    }
    
    override fun onPause() {
        super.onPause()
        orientationService.stop()
    }
}