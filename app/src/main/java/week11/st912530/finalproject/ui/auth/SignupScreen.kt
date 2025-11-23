package week11.st912530.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.IconButton
import android.util.Patterns
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st912530.finalproject.viewmodel.AuthState
import week11.st912530.finalproject.viewmodel.AuthViewModel

@Composable
fun SignupScreen(navController: NavHostController, vm: AuthViewModel) {

    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var msg by remember { mutableStateOf("") }

    val state = vm.authState
    val isLoading = state is AuthState.Loading

    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val isFirstValid = first.isNotBlank()
    val isLastValid = last.isNotBlank()

    when (state) {
        is AuthState.Success -> {
            if (state.uid == "signup_ok") {
                LaunchedEffect(Unit) {
                    msg = "Sign up successful! Please login."
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
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
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)

            if (msg.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(msg, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = first,
                onValueChange = { first = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = first.isNotBlank() && !isFirstValid
            )
            if (first.isNotBlank() && !isFirstValid) {
                Text(
                    text = "Please enter first name",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = last,
                onValueChange = { last = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = last.isNotBlank() && !isLastValid
            )
            if (last.isNotBlank() && !isLastValid) {
                Text(
                    text = "Please enter last name",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
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
            Spacer(Modifier.height(12.dp))
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
                onClick = { vm.signup(first, last, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && isFirstValid && isLastValid && isEmailValid && isPasswordValid
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Register")
                }
            }
        }
    }
}