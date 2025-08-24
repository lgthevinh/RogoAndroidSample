package com.sample.clone.rogo.dialog.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import com.sample.clone.rogo.adapter.device.WiFiListAdapter
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.platform.entity.IoTWifiInfo

class WifiListDialog(
    private val deviceInfo: IoTDirectDeviceInfo,
    private val wifiList: Collection<IoTWifiInfo>
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_wifilist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wifiListView = view.findViewById<RecyclerView>(R.id.recycler_view)
        wifiListView.adapter = WiFiListAdapter(wifiList as MutableCollection<IoTWifiInfo>, object: (IoTWifiInfo) -> Unit {
            override fun invoke(wifiInfo: IoTWifiInfo) {
                dismiss()
                val connectWifiDialog = ConnectWifiDialog(deviceInfo, wifiInfo)
                connectWifiDialog.show(parentFragmentManager, "ConnectWifiDialog")
            }
        })
        wifiListView.layoutManager = LinearLayoutManager(context)
    }
}