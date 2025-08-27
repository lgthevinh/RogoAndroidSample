package com.sample.clone.rogo.fragment.device.wile

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.device.DeviceFoundDialog
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.DiscoverySmartDeviceCallback

class ScanWileFragment: Fragment() {
    private val discoveryHandler = SmartSdk.discoverySmartDeviceHandler()

    private var deviceInfoMap: MutableMap<String, IoTDirectDeviceInfo> = mutableMapOf()

    private lateinit var sharedViewHolder: SharedViewHolder

    // View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewHolder = ViewModelProvider(requireActivity()).get(SharedViewHolder::class.java)
        return inflater.inflate(R.layout.fragment_scanwile, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar: ProgressBar = view.findViewById(R.id.progressBar2)

        // Check if app permission for nearby device is granted, and location and bluetooth is enabled


        view.findViewById<Button>(R.id.config_wile_button).setOnClickListener {
            deviceInfoMap.clear()
            if (checkPermissionsAndSettings(requireContext())) {
                progressBar.visibility = View.VISIBLE

                discoveryHandler.stopDiscovery()
                discoveryHandler.discovery(object : DiscoverySmartDeviceCallback {
                    override fun onSmartDeviceFound(p0: IoTDirectDeviceInfo?) {
                        p0?.let {
                            println("Device Found: ${it.productId}, ${SmartSdk.getProductModel(it.productId).name}, ${it.typeConnect}")
                            if (SmartSdk.getProductModel(p0.productId) != null && p0.typeConnect != IoTDirectDeviceInfo.TypeConnect.MESH) {
                                progressBar.visibility = View.GONE

                                sharedViewHolder.setDeviceInfoData(p0)

                                if (p0.typeConnect == IoTDirectDeviceInfo.TypeConnect.WILEDIRECTLAN) {
                                    val deviceFoundDialog = DeviceFoundDialog("Device Found", p0) {
                                        Navigation.findNavController(view).navigate(R.id.action_scanWileFragment_to_setupWileLanDirectFragment)
                                    }
                                    deviceFoundDialog.show(parentFragmentManager, "deviceFoundDialog")

                                } else {
                                    val deviceFoundDialog = DeviceFoundDialog("Device Found", p0) {
                                        Navigation.findNavController(view).navigate(R.id.action_scanWileFragment_to_setupWileFragment)
                                    }
                                    deviceFoundDialog.show(parentFragmentManager, "deviceFoundDialog")
                                }

                                discoveryHandler.stopDiscovery()
                            }
                        }
                    }

                    override fun onSmartDeviceRemove(p0: String?) {
                        TODO("Not yet implemented")
                    }
                })
            } else {
                // Show android dialog for turn on bluetooth, location and grant access
            }

            view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                discoveryHandler.stopDiscovery()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        discoveryHandler.stopDiscovery()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkPermissionsAndSettings(context: Context): Boolean {
        // Check Nearby Devices permission (Android 12+)
        val nearbyPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED

        // Check Location permission
        val locationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check if Bluetooth is enabled
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

        // Check if Location is enabled
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        // Show a message if any condition is not met
        if (!nearbyPermissionGranted) {
            Toast.makeText(context, "Nearby Devices permission is not granted.", Toast.LENGTH_SHORT).show()
        }
        if (!locationPermissionGranted) {
            Toast.makeText(context, "Location permission is not granted.", Toast.LENGTH_SHORT).show()
        }
        if (!isBluetoothEnabled) {
            Toast.makeText(context, "Bluetooth is not enabled.", Toast.LENGTH_SHORT).show()
        }
        if (!isLocationEnabled) {
            Toast.makeText(context, "Location is not enabled.", Toast.LENGTH_SHORT).show()
        }

        return nearbyPermissionGranted && locationPermissionGranted && isBluetoothEnabled && isLocationEnabled
    }
}