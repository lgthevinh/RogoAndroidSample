package com.sample.clone.rogo.dialog.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sample.clone.rogo.R

class ErrorDialog(title: String?, message: String?): DialogFragment() {
    private val title = title?: "Error"
    private val message = message?: "Unknown error"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_common, container, false)

        val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        titleTextView.text = this.title

        val messageTextView = view.findViewById<TextView>(R.id.message_text_view)
        messageTextView.text = this.message

        val okButton = view.findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}