package com.sample.clone.rogo.dialog.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.PixelCopy.Request
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.define.IoTGroupType
import rogo.iot.module.rogocore.sdk.entity.IoTGroup

class EditGroupDialog(
    private val iotGroup: IoTGroup,
    private val onEditClick: (IoTGroup, String?,  String?) -> Unit,
    private val onDeleteClick: (IoTGroup) -> Unit
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_editgroup, container, false)

        val ioTGroupType = IoTGroupType.entries.toTypedArray()
        val ioTGroupTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ioTGroupType)
        ioTGroupTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner)
        descriptionSpinner.adapter = ioTGroupTypeAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner).selectedItem as IoTGroupType
            val label = view.findViewById<EditText>(R.id.label_edit_text).text.toString()
            onEditClick.invoke(iotGroup, label, descriptionSpinner.name)
            dismiss()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        deleteButton.setOnClickListener {
            onDeleteClick.invoke(iotGroup)
            dismiss()
        }
    }
}