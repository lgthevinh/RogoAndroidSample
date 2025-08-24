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
import com.sample.clone.rogo.dialog.common.SuccessDialog
import com.sample.clone.rogo.utils.JsonUtils.toJsonObject
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

class SignUpFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpButton = view.findViewById<View>(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.name_edit_text).text.toString()
            val email = view.findViewById<EditText>(R.id.email_edit_text).text.toString()
            val phoneNumber = view.findViewById<EditText>(R.id.phone_number_edit_text).text.toString()
            val password = view.findViewById<EditText>(R.id.password_edit_text).text.toString()
            SmartSdk.signUp(name, email, phoneNumber, password, object: AuthRequestCallback {
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
                    }, null)
                    otpDialog.show(parentFragmentManager, "otpDialog")
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    val errorDialog = ErrorDialog("Sign Up Error",
                        errorMessage?.let { it1 -> toJsonObject(it1).get("message").asString } ?: "Unable to sign up, please try again later. Error code: $errorCode"
                    )
                    errorDialog.show(parentFragmentManager, "errorDialog")
                }
            })
        }

        val toSignInButton = view.findViewById<View>(R.id.to_sign_in_button)
        toSignInButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment)
            println("To sign in button clicked")
        }
    }
}