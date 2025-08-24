package com.sample.clone.rogo.fragment.device.ble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.viewmodel.SharedViewHolder
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.CheckListDeviceAvailableCallback
import rogo.iot.module.rogocore.sdk.entity.IoTDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckCenterDeviceFragment: Fragment() {
    private var centerDeviceListView: ListView? = null
    private var centerDeviceAdapter: ArrayAdapter<IoTDevice>? = null

    private lateinit var deviceViewModel: SharedViewHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SmartSdk.configMeshDeviceHandler().checkMeshGatewayAvailable(15, object: CheckListDeviceAvailableCallback {
            override fun onDeviceAvailable(p0: MutableCollection<IoTDevice>?) {
                p0?.let {
                    centerDeviceAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.toList())
                }
            }
        })
        deviceViewModel = ViewModelProvider(requireActivity()).get(SharedViewHolder::class.java)
        return inflater.inflate(R.layout.fragment_checkcenterdevice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        centerDeviceListView = view.findViewById(R.id.device_listview)
        centerDeviceListView?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device = centerDeviceAdapter?.getItem(position)
            deviceViewModel.setDeviceData(device!!)
            Navigation.findNavController(view).navigate(R.id.action_checkCenterDeviceFragment_to_scanBleFragment)
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(20000)
            centerDeviceListView?.adapter = centerDeviceAdapter
            if (centerDeviceAdapter?.count == 0 || centerDeviceAdapter == null) {
                view.findViewById<LinearLayout>(R.id.wait_device_layout).visibility = LinearLayout.GONE
                view.findViewById<LinearLayout>(R.id.no_device_found_layout).visibility = LinearLayout.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.wait_device_layout).visibility = LinearLayout.GONE
                centerDeviceListView?.visibility = LinearLayout.VISIBLE
            }
            println(centerDeviceAdapter?.count)
        }
    }
}