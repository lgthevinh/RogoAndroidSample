package org.thingai.meo.android.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.thingai.meo.android.app.ui.dialogs.auth.OtpInputDialog
import org.thingai.meo.android.app.ui.dialogs.common.ErrorDialog
import org.thingai.meo.android.app.ui.dialogs.common.LoaderDialog
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var showLoaderDialog by remember { mutableStateOf(false) }

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
                text = "Forgot Password",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    SmartSdk.forgot(email, object: AuthRequestCallback {
                        override fun onSuccess() {
                            showOtpDialog = true
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            showErrorDialog = true
                            message = p1 ?: "An unknown error occurred."
                        }

                    })
                    message = "If this email exists, a password reset link has been sent."
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send reset link")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (message.isNotEmpty()) {
                Text(message, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Back to Login")
            }
        }
    }

    if (showOtpDialog) {
        OtpInputDialog(
            onConfirm = {code ->
                showOtpDialog = false
                showLoaderDialog = true
                SmartSdk.updatePasswordOrVerifyAccount(
                    code, newPassword, object: AuthRequestCallback {
                        override fun onSuccess() {
                            navController.navigate("location")
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            showErrorDialog = true
                            message = p1 ?: "An unknown error occurred."
                        }

                    }
                )

            },
            onDismiss = {
                showOtpDialog = false
            }
        )
    }
    ErrorDialog(
        show = showErrorDialog,
        message = message,
        onDismiss = { showErrorDialog = false }
    )
    LoaderDialog(
        show = showLoaderDialog,
        message = "Verifying OTP...",
        onDismiss = { showLoaderDialog = false }
    )
}