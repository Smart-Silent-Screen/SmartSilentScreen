package week11.st912530.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

    // Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFF6F7FF),
                        Color(0xFFEDEFFF)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // App Name
                Text(
                    "Smart Silent Screen",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF3A3A3A)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Automatic silence, smarter focus.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(32.dp))

                if (msg.isNotEmpty()) {
                    Text(msg, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                }

                //Email field with icon
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = email.isNotBlank() && !isEmailValid
                )

                if (email.isNotBlank() && !isEmailValid) {
                    Text(
                        "Please enter a valid email",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                //Password field with icon
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val icon =
                                if (passwordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff
                            Icon(icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = password.isNotBlank() && !isPasswordValid
                )

                if (password.isNotBlank() && !isPasswordValid) {
                    Text(
                        "Password must be at least 6 characters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { vm.login(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && isEmailValid && isPasswordValid,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else Text("Login")
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("No account? Sign up")
                }
            }
        }
    }
}