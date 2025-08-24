package com.sample.clone.rogo.fragment.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.ErrorDialog
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk

class SignInFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInButton = view.findViewById<View>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.email_edit_text).text.toString()
            val password = view.findViewById<EditText>(R.id.password_edit_text).text.toString()
            println("Email: $email, Password: $password")
            SmartSdk.signIn(null, email, null, password, object: AuthRequestCallback {
                override fun onSuccess() {
                    Navigation.findNavController(view).navigate(R.id.action_to_locationManagerFragment)
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    val errorDialog = ErrorDialog("Sign In Error", "Unable to sign in, please try again later. Error code: $errorCode")
                    errorDialog.show(parentFragmentManager, "errorDialog")
                }
            })
        }

        val toSignUpButton = view.findViewById<View>(R.id.to_sign_up_button)
        toSignUpButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        val toForgotPasswordButton = view.findViewById<View>(R.id.to_forgot_password_button)
        toForgotPasswordButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
    }
}