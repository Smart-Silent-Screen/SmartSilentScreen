package week11.st912530.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.util.Patterns
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.viewmodel.AuthState
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController, vm: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }

    val state = vm.authState
    val isLoading = state is AuthState.Loading

    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6

    when (state) {
        is AuthState.Success -> {
            LaunchedEffect(Unit) {
                if (state.uid != "signup_ok") {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
        is AuthState.Error -> msg = state.message
        else -> {}
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            if (msg.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(msg, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = email.isNotBlank() && !isEmailValid
            )

            if (email.isNotBlank() && !isEmailValid) {
                Text(
                    text = "Please enter a valid email",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        val vis = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        Icon(imageVector = vis, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                isError = password.isNotBlank() && !isPasswordValid
            )

            if (password.isNotBlank() && !isPasswordValid) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { vm.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && isEmailValid && isPasswordValid
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = { navController.navigate("signup") }) {
                Text("No account? Sign up")
            }
        }
    }
}