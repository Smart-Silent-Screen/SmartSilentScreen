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
fun LoginScreen(navController: NavHostController, vm: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }

    when (val state = vm.authState) {
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

    ScreenContainer(horizontalAlignment = Alignment.CenterHorizontally) {
        ScreenHeader("Login")

        if (msg.isNotEmpty()) {
            CommonSpacing.Small()
            ErrorMessage(msg)
        }

        CommonSpacing.Large()

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
            onClick = { vm.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        CommonSpacing.Medium()

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("No account? Sign up")
        }
    }
}