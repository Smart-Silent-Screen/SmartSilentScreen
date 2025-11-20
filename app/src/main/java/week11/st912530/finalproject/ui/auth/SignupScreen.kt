package week11.st912530.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import week11.st912530.finalproject.ui.components.*
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

    ScreenContainer(horizontalAlignment = Alignment.CenterHorizontally) {
        ScreenHeader("Create Account")

        if (msg.isNotEmpty()) {
            CommonSpacing.Small()
            InfoMessage(msg)
        }

        CommonSpacing.Large()

        NameTextField(
            value = first,
            onValueChange = { first = it },
            label = "First Name"
        )
        
        CommonSpacing.Medium()
        
        NameTextField(
            value = last,
            onValueChange = { last = it },
            label = "Last Name"
        )
        
        CommonSpacing.Medium()
        
        EmailTextField(
            value = email,
            onValueChange = { email = it }
        )
        
        CommonSpacing.Medium()
        
        PasswordTextField(
            value = password,
            onValueChange = { password = it }
        )

        CommonSpacing.Large()

        Button(
            onClick = { vm.signup(first, last, email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}