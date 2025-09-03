package org.thingai.meo.android.app.ui.dialogs.common

import androidx.compose.runtime.Composable

@Composable
fun SuccessDialog(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        show = show,
        title = "Success",
        message = message,
        onDismiss = onDismiss,
        confirmText = "OK"
    )
}