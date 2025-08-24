package com.sample.clone.rogo.dialog.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment

class EditDeviceDialog(
    private val onEditClick: (String) -> Unit,
    private val onDeleteClick: () -> Unit
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_editdevice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.edit_button).setOnClickListener {
            val label = view.findViewById<EditText>(R.id.label_edit_text).text.toString()
            onEditClick(label)
            dismiss()
        }

        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
            onDeleteClick()
            dismiss()
        }
    }
}