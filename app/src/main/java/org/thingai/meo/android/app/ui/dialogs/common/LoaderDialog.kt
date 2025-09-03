package org.thingai.meo.android.app.ui.dialogs.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoaderDialog(
    show: Boolean,
    message: String = "Loading...",
    onDismiss: () -> Unit = {}
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(message) },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = {}
        )
    }
}