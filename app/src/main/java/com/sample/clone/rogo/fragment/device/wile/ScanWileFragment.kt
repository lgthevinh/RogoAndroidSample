package com.sample.clone.rogo.fragment.device.wile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar: ProgressBar = view.findViewById(R.id.progressBar2)

        view.findViewById<Button>(R.id.config_wile_button).setOnClickListener {
            deviceInfoMap.clear()
            progressBar.visibility = View.VISIBLE

            discoveryHandler.stopDiscovery()
            discoveryHandler.discovery(object : DiscoverySmartDeviceCallback {
                override fun onSmartDeviceFound(p0: IoTDirectDeviceInfo?) {
                    p0?.let {
                        println("Device Found: ${it.productId}, ${it.label}, ${it.typeConnect}")
                        if (SmartSdk.getProductModel(p0.productId) != null && p0.typeConnect != IoTDirectDeviceInfo.TypeConnect.MESH) {
                            discoveryHandler.stopDiscovery()
                            progressBar.visibility = View.GONE

                            if (p0.typeConnect == IoTDirectDeviceInfo.TypeConnect.WILEDIRECTLAN) {
                                val deviceFoundDialog = DeviceFoundDialog("Device Found", p0) {
                                    sharedViewHolder.setDeviceInfoData(p0)
                                    Navigation.findNavController(view).navigate(R.id.action_scanWileFragment_to_setupWileLanDirectFragment)
                                }
                                deviceFoundDialog.show(parentFragmentManager, "deviceFoundDialog")
                            } else {
                                val deviceFoundDialog = DeviceFoundDialog("Device Found", p0) {
                                    sharedViewHolder.setDeviceInfoData(p0)
                                    Navigation.findNavController(view).navigate(R.id.action_scanWileFragment_to_setupWileFragment)
                                }
                                deviceFoundDialog.show(parentFragmentManager, "deviceFoundDialog")
                            }


                        }
                    }
                }

                override fun onSmartDeviceRemove(p0: String?) {
                    TODO("Not yet implemented")
                }
            })

            view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                discoveryHandler.stopDiscovery()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        discoveryHandler.stopDiscovery()
    }
}