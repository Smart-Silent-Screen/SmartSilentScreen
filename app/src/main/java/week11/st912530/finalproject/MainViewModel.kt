package week11.st912530.finalproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import week11.st912530.finalproject.sensor.OrientationService

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val orientationService = OrientationService(application)
}
