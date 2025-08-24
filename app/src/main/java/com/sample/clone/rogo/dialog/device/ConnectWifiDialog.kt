package com.sample.clone.rogo.dialog.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.InfoDialog
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.platform.callback.SuccessRequestCallback
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.platform.entity.IoTWifiInfo
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class ConnectWifiDialog(
    private val deviceInfo: IoTDirectDeviceInfo,
    private val wifiInfo: IoTWifiInfo,
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_connectwifi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            SmartSdk.configWileDirectDeviceHandler().cancel()
            dismiss()
        }

        view.findViewById<Button>(R.id.connect_button).setOnClickListener {
            val password = view.findViewById<EditText>(R.id.password_edit_text).text.toString()
            val deviceLabel = view.findViewById<EditText>(R.id.device_label_edit_text).text.toString()
            SmartSdk.configWileDirectDeviceHandler().requestConnectWifiNetwork(0, wifiInfo.ssid, password, true, object: SuccessRequestCallback {
                override fun onSuccess() {
                    SmartSdk.configWileDirectDeviceHandler().setupAndSyncDeviceToCloud(deviceLabel, null, SmartSdk.getProductModel(deviceInfo.productId).devSubType, object: RequestCallback<IoTDevice> {
                        override fun onSuccess(p0: IoTDevice?) {
                            Navigation.findNavController(view).navigate(R.id.action_to_generalFragment)
                            println("Device setup and sync success")
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            println("Device setup and sync failure")
                        }
                    })
                }

                override fun onFailure(p0: Int, p1: String?) {
                    println("Connect wifi failure")
                }
            })
            dismiss()
        }


    }
}