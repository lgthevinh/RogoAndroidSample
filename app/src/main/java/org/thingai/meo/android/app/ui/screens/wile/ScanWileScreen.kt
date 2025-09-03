package org.thingai.meo.android.app.ui.screens.wile

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.thingai.meo.android.app.ui.dialogs.devices.WileDeviceInfoDialog
import rogo.iot.module.platform.ILogR
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.DiscoverySmartDeviceCallback

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("CoroutineCreationDuringComposition", "MissingPermission")
@Composable
fun ScanWileScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var showPermissionDialog by remember { mutableStateOf(false) }
    var showBluetoothDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showWileDeviceInfoDialog by remember { mutableStateOf(false) }

    var isPermissionGranted by remember { mutableStateOf(hasNearbyPermissions(context)) }

    var deviceInfo by remember { mutableStateOf<IoTDirectDeviceInfo?>(null) }

    fun runDiscovery() {
        if (isPermissionGranted && isBluetoothEnabled() && isLocationEnabled(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                ILogR.D("ScanWileScreen", "All permissions granted and settings enabled. Starting discovery.")
                delay(1000L)
                SmartSdk.discoverySmartDeviceHandler().discovery(object : DiscoverySmartDeviceCallback {
                    override fun onSmartDeviceFound(p0: IoTDirectDeviceInfo?) {
                        p0?.let {
                            ILogR.D(
                                "ScanWileScreen",
                                "onSmartDeviceFound",
                                "Device Found: ${it.productId}, ${SmartSdk.getProductModel(it.productId).name}, ${it.typeConnect}"
                            )
                            if (SmartSdk.getProductModel(p0.productId) != null && p0.typeConnect != IoTDirectDeviceInfo.TypeConnect.MESH) {
                                deviceInfo = p0
                                showWileDeviceInfoDialog = true
                                SmartSdk.discoverySmartDeviceHandler().stopDiscovery()
                            }
                        }
                    }
                    override fun onSmartDeviceRemove(p0: String?) {

                    }
                })
            }
        } else if (!isPermissionGranted) {
            showPermissionDialog = true
        } else if (!isBluetoothEnabled()) {
            showBluetoothDialog = true
        } else if (!isLocationEnabled(context)) {
            showLocationDialog = true
        }
    }
    runDiscovery()



    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // react to permission results
        if (permissionsMap.values.all { it }) {
            runDiscovery()
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Bluetooth and Nearby device permissions are required to scan devices.") },
            confirmButton = {
                Button(onClick = {
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.NEARBY_WIFI_DEVICES,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ))
                    showPermissionDialog = false
                }) { Text("Grant Permissions") }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Bluetooth enable dialog
    if (showBluetoothDialog) {
        AlertDialog(
            onDismissRequest = { showBluetoothDialog = false },
            title = { Text("Enable Bluetooth") },
            text = { Text("Bluetooth is required to scan devices. Please turn it on.") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    context.startActivity(intent)
                    showBluetoothDialog = false
                }) { Text("Turn On Bluetooth") }
            },
            dismissButton = {
                Button(onClick = { showBluetoothDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Location enable dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Enable Location") },
            text = { Text("Location is required to discover nearby Bluetooth devices. Please enable location services.") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                    showLocationDialog = false
                }) { Text("Open Location Settings") }
            },
            dismissButton = {
                Button(onClick = { showLocationDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showWileDeviceInfoDialog) {
        deviceInfo?.let {
            WileDeviceInfoDialog(
                show = showWileDeviceInfoDialog,
                deviceInfo = it,
                onDismiss = {
                    showWileDeviceInfoDialog = false
                    SmartSdk.discoverySmartDeviceHandler().stopDiscovery()
                    navController.popBackStack()
                },
                onConfirm = { deviceInfo ->
                    if (deviceInfo.typeConnect == IoTDirectDeviceInfo.TypeConnect.WILEDIRECTLAN) {
                        // TODO: Navigate to Wile Lan Direct setup
                    } else {
                        // TODO: Navigate to Wile setup
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(48.dp)
                        .alpha(1f),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Scanning Wile device",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Put your phone nearby device you want to add",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                    SmartSdk.discoverySmartDeviceHandler().stopDiscovery()
                    navController.popBackStack()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(text = "Cancel", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp)
            }
        }
    }

}

@Composable
fun WifiPulseIcon() {
    Box(
        modifier = Modifier
            .size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF2947FF).copy(alpha = 0.10f), Color.Transparent),
                        radius = 110f
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                )
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF2947FF).copy(alpha = 0.20f), Color.Transparent),
                        radius = 70f
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                )
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.extraLarge),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "Bluetooth",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun hasNearbyPermissions(context: Context): Boolean {
    val nearbyPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.NEARBY_WIFI_DEVICES // For Android 13+
    ) == PackageManager.PERMISSION_GRANTED

    ILogR.D(
        "ScanWileScreen",
        "hasNearbyPermissions",
        "Nearby Permission: $nearbyPermission",
        "API Level: ${Build.VERSION.SDK_INT}",
        "Permission Check: ${ContextCompat.checkSelfPermission(
            context, Manifest.permission.NEARBY_WIFI_DEVICES
        )}"
    )


    return nearbyPermission
}

fun isBluetoothEnabled(): Boolean {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    return bluetoothAdapter?.isEnabled == true
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true ||
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun ScanWileScreenPreview() {
    val dummyNavController = androidx.navigation.compose.rememberNavController()
    ScanWileScreen(navController = dummyNavController)
}