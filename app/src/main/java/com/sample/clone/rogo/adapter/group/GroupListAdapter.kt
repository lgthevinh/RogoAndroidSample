package com.sample.clone.rogo.adapter.group

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import rogo.iot.module.rogocore.sdk.entity.IoTGroup

class GroupListAdapter(
    private val groupList: MutableCollection<IoTGroup>,
    private val onItemClickCallback: (IoTGroup) -> Unit,
    private val onEditClickCallback: (IoTGroup) -> Unit
): ListAdapter<IoTGroup, GroupListAdapter.GroupListViewHolder>(object: DiffUtil.ItemCallback<IoTGroup>() {
    override fun areItemsTheSame(oldItem: IoTGroup, newItem: IoTGroup): Boolean {
        return oldItem.uuid == newItem.uuid && oldItem.label == newItem.label && oldItem.desc == newItem.desc
    }

    override fun areContentsTheSame(oldItem: IoTGroup, newItem: IoTGroup): Boolean {
        return oldItem == newItem && oldItem.label == newItem.label && oldItem.desc == newItem.desc
    }
})  {
    inner class GroupListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val groupLabel: TextView = view.findViewById(R.id.group_label_text_view)
        val groupDescription: TextView = view.findViewById(R.id.group_description_text_view)
        val editGroupButton: ImageButton = view.findViewById(R.id.edit_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val view = GroupListViewHolder(View.inflate(parent.context, R.layout.item_group, null))
        view.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        val group = groupList.elementAt(position)
        holder.groupLabel.text = group.label
        holder.groupDescription.text = group.desc ?: "Function group"
        holder.itemView.setOnClickListener {
            onItemClickCallback(group)
        }
        holder.editGroupButton.setOnClickListener {
            onEditClickCallback(group)
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun getItem(position: Int): IoTGroup {
        return groupList.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addGroup(group: IoTGroup) {
        groupList.add(group)
    }

    fun removeGroup(group: IoTGroup) {
        groupList.remove(group)
    }

    fun updateGroup(group: IoTGroup) {
        // Remove the group from the list and add it back to the list
        // BUG: The group is not updated in the list
        groupList.find { it.uuid == group.uuid }?.let {
            groupList.remove(it)
            groupList.add(group)
        }
    }

}