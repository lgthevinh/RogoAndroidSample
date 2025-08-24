package com.sample.clone.rogo.adapter.device

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import rogo.iot.module.platform.entity.IoTWifiInfo

class WiFiListAdapter(
    private val wifiList: MutableCollection<IoTWifiInfo>,
    private val onItemClickCallback: (IoTWifiInfo) -> Unit
): ListAdapter<IoTWifiInfo, WiFiListAdapter.WiFiListViewHolder>(object: DiffUtil.ItemCallback<IoTWifiInfo>() {
    override fun areItemsTheSame(oldItem: IoTWifiInfo, newItem: IoTWifiInfo): Boolean {
        return oldItem.ssid == newItem.ssid && oldItem.authType == newItem.authType
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IoTWifiInfo, newItem: IoTWifiInfo): Boolean {
        return oldItem == newItem
    }


}) {
    inner class WiFiListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val wifiLabel: TextView = view.findViewById(R.id.wifi_ssid)
        val wifiSignal: TextView = view.findViewById(R.id.wifi_rssi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WiFiListViewHolder {
        val view = WiFiListViewHolder(View.inflate(parent.context, R.layout.item_wifi, null))
        view.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view
    }

    override fun onBindViewHolder(holder: WiFiListViewHolder, position: Int) {
        val wifi = wifiList.elementAt(position)
        holder.wifiLabel.text = wifi.ssid
        holder.wifiSignal.text = wifi.rssi.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback(wifi)
        }
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }

    override fun getItem(position: Int): IoTWifiInfo {
        return wifiList.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}