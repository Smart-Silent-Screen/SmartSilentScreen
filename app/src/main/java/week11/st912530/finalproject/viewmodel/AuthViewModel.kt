package week11.st912530.finalproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.repository.AuthRepository
import week11.st912530.finalproject.data.repository.IAuthRepository

class AuthViewModel(
    private val repository: IAuthRepository = AuthRepository()
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var userProfile by mutableStateOf<Map<String, Any>?>(null)
        private set

    fun signup(first: String, last: String, email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.signup(first, last, email, password)
                authState = AuthState.Success("signup_ok")
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Signup error")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            authState = AuthState.Error("Missing fields")
            return
        }

        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                val uid = repository.login(email, password)
                userProfile = repository.getUserProfile(uid)
                authState = AuthState.Success(uid)
            } catch (e: Exception) {
                authState = AuthState.Error("Invalid email or password")
            }
        }
    }

    fun logout() {
        repository.logout()
        authState = AuthState.Idle
    }
}