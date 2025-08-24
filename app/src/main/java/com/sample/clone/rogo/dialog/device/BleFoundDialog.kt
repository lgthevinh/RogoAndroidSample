package com.sample.clone.rogo.dialog.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTBleScanned

class BleFoundDialog(
    title: String?,
    private val bleScanned: IoTBleScanned,
    private val onSetupCallback: () -> Unit
): AppDialogFragment() {
    private val title = title?: "Device Found"

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_devicefound, container, false)

        val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        titleTextView.text = this.title

        val messageTextView = view.findViewById<TextView>(R.id.message_text_view)
        messageTextView.text = "Device found: ${bleScanned.ioTProductModel?.name}"

        view.findViewById<Button>(R.id.setup_button).setOnClickListener {
            onSetupCallback()
            dismiss()
        }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}