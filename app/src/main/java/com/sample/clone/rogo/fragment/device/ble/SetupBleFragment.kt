package com.sample.clone.rogo.fragment.device.ble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.clone.rogo.R
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.QuickSetupDeviceCallback
import rogo.iot.module.rogocore.sdk.entity.IoTBleScanned
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class SetupBleFragment: Fragment() {
    private lateinit var sharedViewHolder: SharedViewHolder

    private var gatewayDevice: IoTDevice? = null
    private var bleScanned: IoTBleScanned? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]
        sharedViewHolder.bleScannedData.observe(viewLifecycleOwner) {
            bleScanned = it
        }
        sharedViewHolder.deviceData.observe(viewLifecycleOwner) {
            gatewayDevice = it
        }
        return inflater.inflate(R.layout.fragment_setupble, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bleDeviceName = view.findViewById<EditText>(R.id.device_name).text.toString()
        view.findViewById<Button>(R.id.setup_button).setOnClickListener {
            SmartSdk.configMeshDeviceHandler().quickSetupAndSyncDeviceToCloud(gatewayDevice?.uuid, bleScanned, bleDeviceName, null, bleScanned?.ioTProductModel?.devSubType!!, object: QuickSetupDeviceCallback {
                override fun onSetupProgress(p0: Int, p1: String?) {
                    println("Progress: $p0, $p1")
                }

                override fun onSuccess(p0: IoTDevice?) {
                    println("Device setup success: ${p0?.label}")
                    findNavController().navigate(R.id.action_to_generalFragment)
                }

                override fun onFailure(p0: Int, p1: String?) {
                    println("Device setup failed: $p0, $p1")
                }

            })
            println("Device Name: ${bleScanned?.ioTProductModel?.name}\nPair with: ${gatewayDevice?.label}")
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {

        }
    }
}