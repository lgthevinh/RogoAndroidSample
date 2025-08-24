package com.sample.clone.rogo.dialog.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.AppDialogFragment
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

class VerifyOTPDialog(private val otpVerifyCallback: (Boolean) -> Unit, private val newPassword: String?): AppDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_verifyotp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val otp = view.findViewById<EditText>(R.id.otp_edit_text).text.toString()
            SmartSdk.updatePasswordOrVerifyAccount(otp, newPassword, object: AuthRequestCallback {
                override fun onSuccess() {
                    otpVerifyCallback.invoke(true)
                    dismiss()
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    otpVerifyCallback.invoke(false)
                    dismiss()
                }
            })
            dismiss()
        }
    }
}