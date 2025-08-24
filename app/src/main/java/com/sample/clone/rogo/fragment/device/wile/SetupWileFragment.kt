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
import com.sample.clone.rogo.dialog.device.WifiListDialog
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.platform.entity.IoTNetworkConnectivity
import rogo.iot.module.platform.entity.IoTWifiInfo
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SetupWileDirectDeviceCallback

class SetupWileFragment: Fragment() {
    private lateinit var sharedViewHolder: SharedViewHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setupwile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewHolder = ViewModelProvider(requireActivity())[SharedViewHolder::class.java]
        sharedViewHolder.deviceInfoData.observe(viewLifecycleOwner) {
            var deviceInfo: IoTDirectDeviceInfo? = null

            it?.let {
                val scanButton = view.findViewById<Button>(R.id.scan_button)
                SmartSdk.configWileDirectDeviceHandler().connectAndIdentifyDevice(it, object: SetupWileDirectDeviceCallback {
                    override fun onDeviceIdentifiedAndReadySetup(
                        p0: String?,
                        p1: String?,
                        p2: MutableCollection<IoTNetworkConnectivity>?
                    ) {
                        scanButton.isEnabled = true
                        scanButton.isClickable = true

                        val infoDialog = InfoDialog("Device Info", "Device ID: $p0\nNetwork Connectivity: $p1")
                        infoDialog.show(parentFragmentManager, "InfoDialog")

                        deviceInfo = it
                    }

                    override fun onProgress(p0: Int, p1: String?) {
                        println("Progress: $p0, $p1")
                    }

                    override fun onSetupFailure(p0: Int, p1: String?) {
                        val errorDialog = ErrorDialog("Error", p1)
                        errorDialog.show(parentFragmentManager, "errorDialog")
                    }
                })

                scanButton.setOnClickListener {
                    SmartSdk.configWileDirectDeviceHandler().scanWifi(30, object: RequestCallback<Collection<IoTWifiInfo>> {
                        override fun onSuccess(p0: Collection<IoTWifiInfo>?) {
                            p0?.let {
                                println("P0 size: ${p0.size}")
                                val wifiListDialog = WifiListDialog(deviceInfo!!, p0)
                                wifiListDialog.show(parentFragmentManager, "WifiListDialog")
                            }
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            val errorDialog = ErrorDialog("Error", p1)
                            errorDialog.show(parentFragmentManager, "errorDialog")
                        }
                    })
                }
            }
        }
    }
}