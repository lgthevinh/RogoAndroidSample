package com.sample.clone.rogo.dialog.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.define.IoTGroupType
import rogo.iot.module.rogocore.sdk.entity.IoTGroup

class NewGroupDialog(
    private val groupCreateCallback: (Boolean, IoTGroup?) -> Unit
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_newgroup, container, false)

        val ioTGroupType = IoTGroupType.entries.toTypedArray()
        val ioTGroupTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ioTGroupType)
        ioTGroupTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner)
        descriptionSpinner.adapter = ioTGroupTypeAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.create_button)
        submitButton.setOnClickListener {
            val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner).selectedItem as IoTGroupType
            val label = view.findViewById<EditText>(R.id.label_edit_text).text.toString()

            val groupType = view.findViewById<RadioGroup>(R.id.radio_group).checkedRadioButtonId

            if (groupType == R.id.room_radio_button) {
                SmartSdk.groupHandler().createGroup(label, descriptionSpinner.toString(), object: RequestCallback<IoTGroup> {
                    override fun onSuccess(response: IoTGroup) {
                        groupCreateCallback.invoke(true, response)
                    }

                    override fun onFailure(errorCode: Int, errorMessage: String?) {
                        groupCreateCallback.invoke(false, null)
                    }
                })
            } else {
                SmartSdk.groupHandler().createGroupCtl(label, object: RequestCallback<IoTGroup> {
                    override fun onSuccess(response: IoTGroup) {
                        groupCreateCallback.invoke(true, response)
                    }

                    override fun onFailure(errorCode: Int, errorMessage: String?) {
                        groupCreateCallback.invoke(false, null)
                    }
                })
            }
            dismiss()
        }

    }
}