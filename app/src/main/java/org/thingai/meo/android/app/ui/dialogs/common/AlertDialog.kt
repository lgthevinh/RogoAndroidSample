package org.thingai.meo.android.app.ui.dialogs.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(
    show: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    confirmText: String = "OK"
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    onConfirm?.invoke() ?: onDismiss()
                }) {
                    Text(confirmText)
                }
            }
        )
    }
}