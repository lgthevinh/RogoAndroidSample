package com.sample.clone.rogo.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sample.clone.rogo.R

class DeviceManagerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_devicemanager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.wile_button).setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.action_deviceManagerFragment_to_configWile)
        }

        view.findViewById<Button>(R.id.ble_button).setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.action_deviceManagerFragment_to_checkCenterDeviceFragment)
        }

        view.findViewById<Button>(R.id.media_box_button).setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.action_deviceManagerFragment_to_setupMediaBoxFragment)
        }

        view.findViewById<Button>(R.id.rf_remote_button).setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.action_deviceManagerFragment_to_checkRFGatewayFragment)
        }
    }
}