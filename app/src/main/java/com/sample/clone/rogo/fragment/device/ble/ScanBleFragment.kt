package com.sample.clone.rogo.fragment.device.wile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.dialog.device.BleFoundDialog
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.DiscoveryIoTBleCallback
import rogo.iot.module.rogocore.sdk.entity.IoTBleScanned

class ScanBleFragment: Fragment() {
    private val discoveryHandler = SmartSdk.configMeshDeviceHandler()
    private lateinit var sharedViewHolder: SharedViewHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewHolder = ViewModelProvider(requireActivity()).get(SharedViewHolder::class.java)
        return inflater.inflate(R.layout.fragment_scanble, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.scan_button).setOnClickListener {
            discoveryHandler.discoveryMeshDevice(object: DiscoveryIoTBleCallback {
                override fun onMeshDeviceFound(p0: IoTBleScanned?) {
                    p0?.let {
                        discoveryHandler.stopDiscovery()
                        val bleFoundDialog = BleFoundDialog("Device Found", p0) {
                            sharedViewHolder.setBleScannedData(p0)
                            Navigation.findNavController(view).navigate(R.id.action_scanBleFragment_to_setupBleFragment)
                        }
                        bleFoundDialog.show(parentFragmentManager, "BleFoundDialog")
                    }

                }

                override fun onRequirePermision() {
                    val infoDialog = InfoDialog("Permission Required", "Please enable location permission to scan BLE device")
                    infoDialog.show(parentFragmentManager, "InfoDialog")
                }

                override fun onRequireEnableBluetooth() {
                    val infoDialog = InfoDialog("Bluetooth Required", "Please enable bluetooth to scan BLE device")
                    infoDialog.show(parentFragmentManager, "InfoDialog")
                }

                override fun onBluetoothError() {
                    val infoDialog = InfoDialog("Bluetooth Error", "Bluetooth error occurred")
                    infoDialog.show(parentFragmentManager, "InfoDialog")
                }
            })
        }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            discoveryHandler.stopDiscovery()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        discoveryHandler.stopDiscovery()
    }
}