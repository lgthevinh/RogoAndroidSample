package com.sample.clone.rogo.fragment.device.wile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.platform.entity.IoTNetworkConnectivity
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SetupWileDirectDeviceCallback
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class SetupWileLanDirectFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setupwilelandirect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]
        sharedViewHolder.deviceInfoData.observe(viewLifecycleOwner) { deviceInfo ->
            deviceInfo?.let {
                val configButton = view.findViewById<Button>(R.id.config_wile_lan_direct_button)

                SmartSdk.configWileDirectDeviceHandler().connectAndIdentifyDevice(deviceInfo, object:
                    SetupWileDirectDeviceCallback {
                    override fun onDeviceIdentifiedAndReadySetup(
                        p0: String?,
                        p1: String?,
                        p2: MutableCollection<IoTNetworkConnectivity>?
                    ) {
                        configButton.isEnabled = true
                        configButton.isClickable = true

                        val infoDialog = InfoDialog("Device Info", "Device ID: $p0\nNetwork Connectivity: $p1")
                        infoDialog.show(parentFragmentManager, "InfoDialog")
                    }

                    override fun onProgress(p0: Int, p1: String?) {
                        println("Progress: $p0, $p1")
                    }

                    override fun onSetupFailure(p0: Int, p1: String?) {
                        val errorDialog = ErrorDialog("Error", p1)
                        errorDialog.show(parentFragmentManager, "errorDialog")
                    }
                })

                configButton.setOnClickListener {

                    // Get device label
                    SmartSdk.configWileDirectDeviceHandler()
                        .setupAndSyncDeviceToCloud(
                            deviceInfo.label,
                            null,
                            SmartSdk.getProductModel(deviceInfo!!.productId).devSubType,
                            object: RequestCallback<IoTDevice> {
                        override fun onSuccess(p0: IoTDevice?) {
                            p0?.let {
                                val successDialog = InfoDialog("Success", "Device setup successful: ${p0.uuid}")
                                successDialog.show(parentFragmentManager, "successDialog")
                            } ?: run {
                                val errorDialog = ErrorDialog("Error", "Device setup failed: No device returned")
                                errorDialog.show(parentFragmentManager, "errorDialog")
                            }
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            val errorDialog = ErrorDialog("Error", p1 ?: "Unknown error during device setup")
                            errorDialog.show(parentFragmentManager, "errorDialog")
                        }

                    })
                }
            }
        }

    }

}