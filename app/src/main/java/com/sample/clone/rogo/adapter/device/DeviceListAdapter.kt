package com.sample.clone.rogo.adapter.device

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTDevice

class DeviceListAdapter(
    private val deviceList: MutableCollection<IoTDevice>,
    private val onItemClickCallback: (IoTDevice) -> Unit,
    private val onEditClickCallback: (IoTDevice) -> Unit
): ListAdapter<IoTDevice, DeviceListAdapter.DeviceListViewHolder>(object: DiffUtil.ItemCallback<IoTDevice>() {
    override fun areItemsTheSame(oldItem: IoTDevice, newItem: IoTDevice): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: IoTDevice, newItem: IoTDevice): Boolean {
        return oldItem == newItem
    }
})  {
    inner class DeviceListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val deviceLabel: TextView = view.findViewById(R.id.device_label_text_view)
        val deviceDescription: TextView = view.findViewById(R.id.device_description_text_view)
        val editDeviceButton: ImageButton = view.findViewById(R.id.edit_device_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListViewHolder {
        val view = DeviceListViewHolder(View.inflate(parent.context, R.layout.item_device, null))
        view.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view
    }

    override fun onBindViewHolder(holder: DeviceListViewHolder, position: Int) {
        val device = deviceList.elementAt(position)
        holder.deviceLabel.text = device.label
        holder.deviceDescription.text = SmartSdk.getProductModel(device.productId).name
        holder.itemView.setOnClickListener {
            onItemClickCallback(device)
        }
        holder.editDeviceButton.setOnClickListener {
            onEditClickCallback(device)
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun getItem(position: Int): IoTDevice {
        return deviceList.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}