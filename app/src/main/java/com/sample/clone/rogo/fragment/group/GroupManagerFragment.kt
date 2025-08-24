package com.sample.clone.rogo.fragment.group

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.PixelCopy.Request
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.clone.rogo.R
import com.sample.clone.rogo.adapter.group.GroupListAdapter
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.InfoDialog
import com.sample.clone.rogo.dialog.group.EditGroupDialog
import com.sample.clone.rogo.dialog.group.NewGroupDialog
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTGroup

class GroupManagerFragment: Fragment() {
    private var groupList: MutableCollection<IoTGroup> = SmartSdk.groupHandler().groupRooms
    private var groupControlList: MutableCollection<IoTGroup> = SmartSdk.groupHandler().groupCtls

    private var groupAdapter: GroupListAdapter? = null
    private var groupControlAdapter: GroupListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groupmanager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Room list
        groupAdapter = GroupListAdapter(groupList, object: (IoTGroup) -> Unit {
            override fun invoke(group: IoTGroup) { // On item click
                val infoDialog = InfoDialog("Group Info", "Group UUID: ${group.uuid}\nGroup Label: ${group.label}\nGroup Description: ${group.desc}")
                infoDialog.show(parentFragmentManager, "InfoDialog")
            }
        }, object: (IoTGroup) -> Unit {
            override fun invoke(group: IoTGroup) {
                val editGroupDialog = EditGroupDialog(group, object: (IoTGroup, String?, String?) -> Unit {
                    override fun invoke(iotGroup: IoTGroup, label: String?, description: String?) {
                        SmartSdk.groupHandler().updateGroup(iotGroup.uuid, label, description, object: RequestCallback<IoTGroup> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(p0: IoTGroup?) {
                                val infoDialog = InfoDialog("Success", "Group updated\nGroup UUID: ${iotGroup.uuid}\nGroup Label: $label\nGroup Description: $description")
                                infoDialog.show(parentFragmentManager, "InfoDialog")

                                // BUG: The group is not updated in the adapter
                                groupAdapter!!.updateGroup(p0!!)
                                groupAdapter!!.notifyDataSetChanged()
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to update group")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                }, object: (IoTGroup) -> Unit {
                    override fun invoke(group: IoTGroup) {
                        SmartSdk.groupHandler().delete(group.uuid, object: RequestCallback<Boolean> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(p0: Boolean?) {
                                val infoDialog = InfoDialog("Success", "Group deleted\nGroup UUID: ${group.uuid}")
                                infoDialog.show(parentFragmentManager, "InfoDialog")

                                groupAdapter!!.removeGroup(group)
                                groupAdapter!!.notifyDataSetChanged()
                            }
                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to delete group")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                })
                editGroupDialog.show(parentFragmentManager, "EditGroupDialog")
            }
        })
        val groupRecyclerView = view.findViewById<RecyclerView>(R.id.group_recycler_view)
        groupRecyclerView.adapter = groupAdapter
        groupRecyclerView.layoutManager = LinearLayoutManager(context)

        // Function group
        groupControlAdapter = GroupListAdapter(groupControlList, object: (IoTGroup) -> Unit {
            override fun invoke(group: IoTGroup) {
                val infoDialog = InfoDialog("Group Info", "Group UUID: ${group.uuid}\nGroup Label: ${group.label}\nGroup Description: Function group")
                infoDialog.show(parentFragmentManager, "InfoDialog")
            }
        }, object: (IoTGroup) -> Unit {
            override fun invoke(group: IoTGroup) {
                val editGroupDialog = EditGroupDialog(group, object: (IoTGroup, String?, String?) -> Unit {
                    override fun invoke(iotGroup: IoTGroup, label: String?, description: String?) {
                        SmartSdk.groupHandler().updateGroup(iotGroup.uuid, label, null, object: RequestCallback<IoTGroup> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(p0: IoTGroup?) {
                                val infoDialog = InfoDialog("Success", "Group updated\nGroup UUID: ${iotGroup.uuid}\nGroup Label: $label")
                                infoDialog.show(parentFragmentManager, "InfoDialog")

                                groupControlAdapter!!.updateGroup(p0!!)
                                groupControlAdapter!!.notifyDataSetChanged()
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to update group")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                }, object: (IoTGroup) -> Unit {
                    override fun invoke(iotGroup: IoTGroup) {
                        SmartSdk.groupHandler().delete(iotGroup.uuid, object: RequestCallback<Boolean> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onSuccess(p0: Boolean?) {
                                val infoDialog = InfoDialog("Success", "Group deleted\nGroup UUID: ${iotGroup.uuid}\nGroup Label: ${iotGroup.label}")
                                infoDialog.show(parentFragmentManager, "InfoDialog")

                                groupControlAdapter!!.removeGroup(group)
                                groupControlAdapter!!.notifyDataSetChanged()
                            }

                            override fun onFailure(p0: Int, p1: String?) {
                                val errorDialog = ErrorDialog("Error", "Failed to delete group")
                                errorDialog.show(parentFragmentManager, "ErrorDialog")
                            }
                        })
                    }
                })
                editGroupDialog.show(parentFragmentManager, "EditGroupDialog")
            }
        })
        val groupControlRecyclerView = view.findViewById<RecyclerView>(R.id.group_control_recycler_view)
        groupControlRecyclerView.adapter = groupControlAdapter
        groupControlRecyclerView.layoutManager = LinearLayoutManager(context)

        // Add group button
        val addGroupButton = view.findViewById<View>(R.id.add_group_button)
        addGroupButton.setOnClickListener {
            val newGroupDialog = NewGroupDialog(object: (Boolean, IoTGroup?) -> Unit {
                @SuppressLint("NotifyDataSetChanged")
                override fun invoke(success: Boolean, group: IoTGroup?) {
                    if (success) {
                        val infoDialog = InfoDialog("Success", "New group created\nGroup UUID: ${group?.uuid}\nGroup Label: ${group?.label}\nGroup Description: ${group?.desc?.ifEmpty { "Function group" }}")
                        infoDialog.show(parentFragmentManager, "InfoDialog")

                        if (group?.desc?.isEmpty() == true) {
                            groupControlAdapter!!.addGroup(group)
                            groupControlAdapter!!.notifyDataSetChanged()
                        } else {
                            group?.let { it1 -> groupAdapter!!.addGroup(it1) }
                            groupAdapter!!.notifyDataSetChanged()
                        }

                    } else {
                        val errorDialog = ErrorDialog("Error", "Failed to create new group")
                        errorDialog.show(parentFragmentManager, "ErrorDialog")
                    }
                }
            })
            newGroupDialog.show(parentFragmentManager, "NewGroupDialog")
        }
    }
}