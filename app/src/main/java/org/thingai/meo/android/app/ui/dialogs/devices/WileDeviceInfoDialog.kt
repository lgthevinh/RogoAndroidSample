package org.thingai.meo.android.app.ui.dialogs.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.rogocore.sdk.SmartSdk

@Composable
fun WileDeviceInfoDialog(
    show: Boolean,
    deviceInfo: IoTDirectDeviceInfo,
    onDismiss: () -> Unit,
    onConfirm: (IoTDirectDeviceInfo) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Device Info") },
        text = {
            Column(modifier = Modifier) {
                Text("Device label: ${SmartSdk.getProductModel(deviceInfo.productId).name ?: "-"}")
                Text("Type connect: ${deviceInfo.typeConnect ?: "-"}")
                Text("Product ID: ${deviceInfo.productId ?: "-"}")
                Text("MAC Address: ${deviceInfo.mac ?: "-"}")
                Text("IP address: ${deviceInfo.ip ?: "-"}")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(deviceInfo) }) {
                Text("Continue setup")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}