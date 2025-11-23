package week11.st912530.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var msg by remember { mutableStateOf("") }

    when (val state = vm.authState) {
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = last,
                onValueChange = { last = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { vm.signup(first, last, email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}