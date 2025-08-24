package com.sample.clone.rogo.fragment.device.rf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.platform.define.IoTDeviceType
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.PairRfDeviceCallback
import rogo.iot.module.rogocore.sdk.entity.IoTPairedRfDevice

class ScanRfDeviceFragment: Fragment() {

    private enum class RemoteType {
        AC_CONTROLLER,
        FAN,
        TV;

        override fun toString(): String {
            return when(this) {
                AC_CONTROLLER -> "AC Controller"
                FAN -> "Fan"
                TV -> "TV"
            }
        }

        fun toValue(): Int {
            return when(this) {
                AC_CONTROLLER -> IoTDeviceType.AC_CONTROLLER
                FAN -> IoTDeviceType.FAN
                TV -> IoTDeviceType.TV
            }
        }
    }

    private lateinit var sharedViewHolder: SharedViewHolder

    private lateinit var gatewayId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]
        sharedViewHolder.rfGatewayIdData.observe(viewLifecycleOwner) {
            gatewayId = it
        }
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_scanrfdevice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remoteSpinner = view.findViewById<Spinner>(R.id.remote_spinner)
        val remoteTypeAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, RemoteType.entries.toTypedArray())
        remoteSpinner.adapter = remoteTypeAdapter

        view.findViewById<Button>(R.id.scan_button).setOnClickListener {
            // Scan RF device
            val remoteType = remoteSpinner.selectedItem as RemoteType

//            SmartSdk.configRfDeviceHandler().startPairingRf(gatewayId, remoteType.toValue(), 30,
//                object : PairRfDeviceCallback {
//                    override fun onPairedDevice(p0: IoTPairedRfDevice?) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onPairingFinished() {
//                        TODO("Not yet implemented")
//                    }
//                }
//            )
        }
    }
}