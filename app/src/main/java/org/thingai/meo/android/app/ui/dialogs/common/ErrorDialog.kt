package org.thingai.meo.android.app.ui.dialogs.common

import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        show = show,
        title = "Error",
        message = message,
        onDismiss = onDismiss,
        confirmText = "Close"
    )
}