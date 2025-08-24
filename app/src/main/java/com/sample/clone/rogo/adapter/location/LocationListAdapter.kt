package com.sample.clone.rogo.adapter.location

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import rogo.iot.module.rogocore.sdk.entity.IoTLocation

class LocationListAdapter(
    private val locationList: MutableCollection<IoTLocation>,
    private val onItemClickCallback: (IoTLocation) -> Unit,
    private val onEditClickCallback: (IoTLocation) -> Unit
): ListAdapter<IoTLocation, LocationListAdapter.LocationViewHolder>(object: DiffUtil.ItemCallback<IoTLocation>() {
    override fun areItemsTheSame(oldItem: IoTLocation, newItem: IoTLocation): Boolean {
        println("oldItem: ${oldItem.uuid}, newItem: ${newItem.uuid}")
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: IoTLocation, newItem: IoTLocation): Boolean {
        return oldItem == newItem
    }
}) {
    inner class LocationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val locationDescription: TextView = view.findViewById(R.id.location_description_text_view)
        val locationLabel: TextView = view.findViewById(R.id.location_label_text_view)
        val editLocationButton: ImageButton = view.findViewById(R.id.edit_location_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LocationViewHolder(View.inflate(parent.context, R.layout.item_location, null))
        // BUG: The item view is not filling the parent view
        // FIX: Set the layout params of the item view to fill the parent view
        view.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList.elementAt(position)
        holder.locationDescription.text = location.desc
        holder.locationLabel.text = location.label
        holder.itemView.setOnClickListener {
            onItemClickCallback(location)
        }
        holder.editLocationButton.setOnClickListener {
            onEditClickCallback(location)
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun getItem(position: Int): IoTLocation {
        return locationList.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addLocation(location: IoTLocation) {
        locationList.add(location)
    }

    fun updateLocation(location: IoTLocation) {
        locationList.remove(location)
        locationList.add(location)
    }
}