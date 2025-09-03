package org.thingai.meo.android.app.ui.dialogs.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OtpInputDialog(
    otpLength: Int = 6,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var otp by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter OTP") },
        text = {
            Column {
                OutlinedTextField(
                    value = otp,
                    onValueChange = {
                        if (it.length <= otpLength) otp = it
                    },
                    label = { Text("OTP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Please enter the $otpLength-digit OTP sent to your device.")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(otp) },
                enabled = otp.length == otpLength
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}