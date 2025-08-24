package com.sample.clone.rogo.dialog

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

abstract class AppDialogFragment: DialogFragment() {
    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}