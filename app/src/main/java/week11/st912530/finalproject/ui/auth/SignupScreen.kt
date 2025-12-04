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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    // Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF6F7FF),
                        Color(0xFFEDEFFF)
                    )
                )
            )
    ) {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // App name
                Text(
                    "Smart Silent Screen",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF3A3A3A)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Create your account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(32.dp))

                if (msg.isNotEmpty()) {
                    Text(msg, color = Color(0xFF0066CC))
                    Spacer(Modifier.height(12.dp))
                }

                // First Name
                OutlinedTextField(
                    value = first,
                    onValueChange = { first = it },
                    label = { Text("First Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = first.isNotBlank() && !isFirstValid
                )

                Spacer(Modifier.height(12.dp))

                // Last Name
                OutlinedTextField(
                    value = last,
                    onValueChange = { last = it },
                    label = { Text("Last Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = last.isNotBlank() && !isLastValid
                )

                Spacer(Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = email.isNotBlank() && !isEmailValid
                )

                Spacer(Modifier.height(12.dp))

                // Password
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

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { vm.signup(first, last, email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading &&
                            isFirstValid &&
                            isLastValid &&
                            isEmailValid &&
                            isPasswordValid,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else Text("Register")
                }
            }
        }
    }
}