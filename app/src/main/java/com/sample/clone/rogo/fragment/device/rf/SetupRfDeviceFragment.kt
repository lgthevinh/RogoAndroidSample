package com.sample.clone.rogo.fragment.device.rf

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
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTDevice
import rogo.iot.module.rogocore.sdk.entity.IoTPairedRfDevice

class SetupRfDeviceFragment: Fragment() {

    private lateinit var sharedViewHolder: SharedViewHolder

    private var pairedRfDevice: IoTPairedRfDevice? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]
        sharedViewHolder.pairedRfDeviceData.observe(viewLifecycleOwner) {
            pairedRfDevice = it
        }
        return inflater.inflate(R.layout.fragment_setuprf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.setup_button).setOnClickListener {
            val deviceName = view.findViewById<EditText>(R.id.device_name).text.toString()
            SmartSdk.configRfDeviceHandler().syncDeviceToCloud(
                deviceName,
                deviceName,
                null,
                pairedRfDevice,
                object: RequestCallback<IoTDevice> {
                    override fun onSuccess(result: IoTDevice) {
                        println("Device setup success: ${result.label}")
                        findNavController().navigate(R.id.action_to_generalFragment)
                    }

                    override fun onFailure(code: Int, message: String) {
                        println("Device setup failed: $code, $message")
                    }
                }
            )
        }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            SmartSdk.configRfDeviceHandler().cancelSetupDevice()
            findNavController().navigate(R.id.action_to_generalFragment)
        }

    }
}