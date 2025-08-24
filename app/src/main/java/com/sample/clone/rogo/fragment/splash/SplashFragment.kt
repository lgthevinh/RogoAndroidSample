package com.sample.clone.rogo.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.sample.clone.rogo.R
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SmartSdkConnectCallback

/**
 * A simple [Fragment] subclass.
 * This fragment is for SmartSDK connecting service.
 */
class SplashFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SmartSdk.connectService(object: SmartSdkConnectCallback {
            override fun onConnected(isAuthenticated: Boolean) {
                if (isAuthenticated) { // If the user is authenticated and location is set, navigate to the main screen
                    if (SmartSdk.getAppLocation() != null) {
                        Navigation.findNavController(view).navigate(R.id.action_to_generalFragment)
                    }
                    else { // If location is not set, navigate to the location manager screen
                        Navigation.findNavController(view).navigate(R.id.action_to_locationManagerFragment)
                    }
                }
                else { // If the user is not authenticated, navigate to the sign-in screen
                    Navigation.findNavController(view).navigate(R.id.action_to_signInFragment)
                }
            }

            override fun onDisconnected() {
                SmartSdk.closeConnectionService()
            }
        })
    }
}