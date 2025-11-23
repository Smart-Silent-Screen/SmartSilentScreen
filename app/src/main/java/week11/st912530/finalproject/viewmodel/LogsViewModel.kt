package week11.st912530.finalproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.model.EventLog
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.FirestoreRepository

class LogsViewModel(
    private val repo: FirestoreRepository = FirestoreRepository(),
    private val authRepo: AuthRepository = AuthRepository()
) : ViewModel() {

    var events by mutableStateOf<List<EventLog>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            authRepo.getAuthState().collectLatest { user ->
                if (user != null) {
                    try {
                        errorMessage = null
                        repo.observeEvents(user.uid).collect { list ->
                            events = list
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                } else {
                    events = emptyList()
                    errorMessage = "User not logged in"
                }
            }
        }
    }
}