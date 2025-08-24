package com.sample.clone.rogo.dialog.rface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment

class RegisterFaceDialog(
    private val onRegister: (String) -> Unit
): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_registerface,
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.label_edit_text)
                .text.toString()
            onRegister(name)
            dismiss()
        }
    }
}