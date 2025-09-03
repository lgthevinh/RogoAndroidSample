package org.thingai.meo.android.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.thingai.meo.android.app.ui.dialogs.common.ErrorDialog
import org.thingai.meo.android.app.ui.dialogs.common.LoaderDialog
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    ErrorDialog(
        show = showErrorDialog,
        message = errorMessage,
        onDismiss = { showErrorDialog = false }
    )

    var showLoaderDialog by remember { mutableStateOf(false) }
    LoaderDialog(
        show = showLoaderDialog,
        message = "Signing in...",
        onDismiss = { showLoaderDialog = false }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    showLoaderDialog = true
                    if (verifyEmailAndPassword(email, password)) {
                        SmartSdk.signIn(null, email, null, password, object: AuthRequestCallback {
                            override fun onSuccess() {
                                navController.navigate("location")
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                errorMessage = "Error code: $p0. Unable to sign in, please try again later with message $p1."
                                showErrorDialog = true
                                showLoaderDialog = false
                            }

                        })
                    } else {
                        errorMessage = "Please enter both email and password correctly"
                        showErrorDialog = true
                        showLoaderDialog = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Sign Up")
                }
                TextButton(onClick = { navController.navigate("forgot_password") }) {
                    Text("Forgot Password?")
                }
            }
        }
    }
}

fun verifyEmailAndPassword(email: String, password: String): Boolean {
    return email.isNotEmpty() && password.isNotEmpty()
}

@Preview
@Composable
fun LoginScreenPreview() {
    val dummyNavController = rememberNavController()
    LoginScreen(navController = dummyNavController)
}