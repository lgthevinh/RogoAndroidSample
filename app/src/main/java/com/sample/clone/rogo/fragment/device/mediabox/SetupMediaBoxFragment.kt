package com.sample.clone.rogo.fragment.device.mediabox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.dialog.common.SuccessDialog
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.RequestAddGatewayCallback
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class SetupMediaBoxFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setupmediabox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SmartSdk.configGatewayDeviceHandler().setupGatewayDevice("verifyId", object: RequestAddGatewayCallback {
            override fun onVerifyCodeGenerated(p0: String?) {
                println("Verify code: $p0")
                // delay 1 second to create loading feeling :)
                Thread.sleep(1000)
                view.findViewById<TextView>(R.id.tv_code).text = p0

                view.findViewById<LinearLayout>(R.id.layout_waiting).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.layout_code).visibility = View.VISIBLE
            }

            override fun onTimeCountDown(p0: Int) {
                println("Time count down: $p0")
                view.findViewById<TextView>(R.id.tv_countdown).text = "Code valid for $p0 seconds"
            }

            override fun onSuccess(p0: IoTDevice?) {
                println("Gateway device setup success: ${p0?.label}")
                val successDialog = SuccessDialog("Gateway device setup success", "Gateway device setup success: ${p0?.label}")
                successDialog.show(childFragmentManager, "successDialog")

            }

            override fun onFailure(p0: Int, p1: String?) {
                println("Gateway device setup failed: $p0, $p1")
                val errorDialog = ErrorDialog("Gateway device setup failed", "Gateway device setup failed: $p1")
                errorDialog.show(childFragmentManager, "errorDialog")
            }

            override fun onCancelled() {
                println("Gateway device setup cancelled")
                val infoDialog = InfoDialog("Gateway device setup cancelled", "Gateway device setup cancelled")
                infoDialog.show(childFragmentManager, "infoDialog")
            }
        })
    }
}