package com.sample.clone.rogo.dialog.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.sample.clone.rogo.R
import com.sample.clone.rogo.adapter.location.LocationListAdapter
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.define.IoTLocationType
import rogo.iot.module.rogocore.sdk.entity.IoTLocation

class EditLocationDialog(
    private val location : IoTLocation,
    private val locationEditCallback: (Boolean, IoTLocation?) -> Unit,
    private val locationDeleteCallback: (Boolean) -> Unit,
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_editlocation, container, false)

        val ioTLocationTypes = IoTLocationType.entries.toTypedArray()
        val ioTLocationTypesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ioTLocationTypes)
        ioTLocationTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner)
        descriptionSpinner.adapter = ioTLocationTypesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner).selectedItem as IoTLocationType
            val label = view.findViewById<EditText>(R.id.label_edit_text).text.toString()
            SmartSdk.locationHandler().updateLocation(location.uuid, label, descriptionSpinner.toString(), object: RequestCallback<IoTLocation> {
                override fun onSuccess(response: IoTLocation) {
                    locationEditCallback.invoke(true, response)
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    locationEditCallback.invoke(false, null)
                }
            })
            dismiss()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        deleteButton.setOnClickListener {
            SmartSdk.locationHandler().delete(location.uuid, object: RequestCallback<Boolean> {
                override fun onSuccess(response: Boolean) {
                    locationDeleteCallback.invoke(true)
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    locationDeleteCallback.invoke(false)
                }
            })
            dismiss()
        }

    }
}