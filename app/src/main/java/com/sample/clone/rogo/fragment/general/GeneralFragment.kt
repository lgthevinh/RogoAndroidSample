package com.sample.clone.rogo.fragment.general

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.PixelCopy.Request
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import com.sample.clone.rogo.adapter.device.DeviceListAdapter
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.dialog.device.EditDeviceDialog
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class GeneralFragment: Fragment() {
    private var deviceList = SmartSdk.deviceHandler().all
    private var deviceAdapter: DeviceListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceAdapter = DeviceListAdapter(deviceList, object: (IoTDevice) -> Unit {
            override fun invoke(device: IoTDevice) {
                val infoDialog = InfoDialog("Device Info", "Device label: ${device.label}\nDevice UID: ${device.uuid}\nDevice EID: ${device.eid}")
                infoDialog.show(parentFragmentManager, "InfoDialog")
            }
        }, object: (IoTDevice) -> Unit {
            override fun invoke(device: IoTDevice) {
                val editDeviceDialog = EditDeviceDialog(object: (String) -> Unit {
                    override fun invoke(label: String) {
                        SmartSdk.deviceHandler().updateDeviceLabel(device.uuid, label, null, object: RequestCallback<IoTDevice> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(result: IoTDevice) {
                                deviceList.remove(device)
                                deviceList.add(result)

                                deviceAdapter?.notifyDataSetChanged()
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to update device label")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                }, object: () -> Unit {
                    override fun invoke() {
                        SmartSdk.deviceHandler().delete(device.uuid, object: RequestCallback<Boolean> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(result: Boolean) {
                                deviceList.remove(device)
                                deviceAdapter?.notifyDataSetChanged()
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to delete device")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                })
                editDeviceDialog.show(parentFragmentManager, "EditDeviceDialog")
            }
        })
        val deviceRecyclerView = view.findViewById<RecyclerView>(R.id.device_recycler_view)
        deviceRecyclerView.adapter = deviceAdapter
        deviceRecyclerView.layoutManager = LinearLayoutManager(context)

        val groupButton = view.findViewById<Button>(R.id.group_button)
        groupButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_groupManagerFragment)
        }

        val deviceButton = view.findViewById<Button>(R.id.device_button)
        deviceButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_deviceManagerFragment)
        }

        view.findViewById<Button>(R.id.rface).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_RFaceFragment)
        }

        view.findViewById<Button>(R.id.iotlink).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_ioTLinkFragment)
        }
    }
}