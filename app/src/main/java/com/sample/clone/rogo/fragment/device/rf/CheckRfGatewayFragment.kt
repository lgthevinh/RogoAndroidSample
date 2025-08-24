package com.sample.clone.rogo.fragment.device.rf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.CheckDeviceAvailableCallback

class CheckRfGatewayFragment: Fragment() {

    private lateinit var sharedViewHolder: SharedViewHolder

    private var waitDeviceLayout: View? = null
    private var noDeviceFoundLayout: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]

        return inflater.inflate(R.layout.fragment_check_rf_gateway, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitDeviceLayout = view.findViewById(R.id.wait_device_layout)
        noDeviceFoundLayout = view.findViewById(R.id.no_device_found_layout)

        SmartSdk.configRfDeviceHandler().checkRfGatewayAvailable(
            object: CheckDeviceAvailableCallback {
            override fun onDeviceAvailable(deviceId: String?) {
                if (deviceId != null) {
                    SmartSdk.configRfDeviceHandler().cancelCheckRfGatewayAvailable()
                    sharedViewHolder.setRfGatewayIdData(deviceId)
                    Navigation.findNavController(view)
                        .navigate(R.id.action_checkRFGatewayFragment_to_scanRfDeviceFragment)
                }
            }
        })

        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            SmartSdk.configRfDeviceHandler().cancelCheckRfGatewayAvailable()

            noDeviceFoundLayout?.visibility = View.VISIBLE
            waitDeviceLayout?.visibility = View.GONE
        }
    }
}