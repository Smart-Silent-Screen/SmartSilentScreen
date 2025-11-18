package week11.st912530.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st912530.finalproject.data.repository.AuthRepository

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var authState: AuthState = AuthState.Idle
        private set

    fun login(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                val uid = repository.login(email, password)
                authState = AuthState.Success(uid)
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Login error")
            }
        }
    }

    fun signup(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                val uid = repository.signup(email, password)
                authState = AuthState.Success(uid)
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Signup error")
            }
        }
    }

    fun logout() {
        repository.logout()
        authState = AuthState.Idle
    }
}