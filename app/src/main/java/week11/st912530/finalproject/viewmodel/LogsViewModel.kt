package week11.st912530.finalproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.model.EventLog
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository
import week11.st912530.finalproject.data.repository.IAuthRepository
import week11.st912530.finalproject.data.repository.IFirestoreRepository

/**
 * ViewModel for event logs display.
 */
class LogsViewModel(
    private val repo: IFirestoreRepository = FirestoreRepository(),
    private val authRepo: IAuthRepository = AuthRepository()
) : ViewModel() {

    var events by mutableStateOf<List<EventLog>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        val uid = authRepo.currentUser()
        if (uid == null) {
            errorMessage = "User not logged in"
        } else {
            viewModelScope.launch {
                try {
                    repo.observeEvents(uid).collect { list ->
                        events = list
                    }
                } catch (e: Exception) {
                    errorMessage = e.message
                }
            }
        }
    }
}