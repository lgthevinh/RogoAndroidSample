package com.sample.clone.rogo.fragment.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.authentication.VerifyOTPDialog
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.utils.JsonUtils.toJsonObject
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

class ForgotPasswordFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgotpassword, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<View>(R.id.submit_button)
        submitButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.email_edit_text).text.toString()
            val newPassword = view.findViewById<EditText>(R.id.new_password_edit_text).text.toString()
            val confirmPassword = view.findViewById<EditText>(R.id.confirm_password_edit_text).text.toString()
            if (newPassword == confirmPassword) { // Should be replaced with a more secure password validation
                SmartSdk.forgot(email, object: AuthRequestCallback {
                    override fun onSuccess() {
                        val otpDialog = VerifyOTPDialog(object : (Boolean) -> Unit {
                            override fun invoke(verified: Boolean) {
                                if (verified) {
                                    Navigation.findNavController(view).navigate(R.id.action_to_locationManagerFragment)
                                } else {
                                    val errorDialog = ErrorDialog("OTP Verification Error", "Unable to verify OTP, please try again later")
                                    errorDialog.show(parentFragmentManager, "errorDialog")
                                }
                            }
                        }, newPassword)
                        otpDialog.show(parentFragmentManager, "otpDialog")
                    }

                    override fun onFailure(errorCode: Int, errorMessage: String?) {
                        val errorDialog = ErrorDialog("Reset Password Error",
                            errorMessage?.let { it1 -> toJsonObject(it1).get("message").asString } ?: "Unable to reset password, please try again later. Error code: $errorCode"
                        )
                        errorDialog.show(parentFragmentManager, "errorDialog")
                    }
                })
            } else {
                val errorDialog = ErrorDialog("Reset Password Error", "Passwords do not match.")
                errorDialog.show(parentFragmentManager, "errorDialog")
            }
        }
    }
}