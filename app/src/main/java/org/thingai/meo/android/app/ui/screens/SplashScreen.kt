package org.thingai.meo.android.app.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import rogo.iot.module.platform.ILogR
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SmartSdkConnectCallback

@Composable
fun SplashScreen(navController: NavController) {
    val TAG: String = "SplashScreen"

    LaunchedEffect(Unit) {
        ILogR.D(TAG, "LaunchedEffect", "SmartSdk connect service")
        SmartSdk.connectService(
            object: SmartSdkConnectCallback {
                override fun onConnected(isAuthenticated: Boolean) {
                    if (isAuthenticated) {
                        if (SmartSdk.getAppLocation() != null) {
                            Handler(Looper.getMainLooper()).post {
                                navController.navigate("home")
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                navController.navigate("location")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            navController.navigate("login")
                        }
                        ILogR.D(TAG, "onConnected", "Not authenticated, navigate to SignInScreen")
                    }
                }

                override fun onDisconnected() {
                    SmartSdk.closeConnectionService()
                    ILogR.D(TAG, "onDisconnected", "SmartSdk disconnected")
                    Handler(Looper.getMainLooper()).post {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Meo Service",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}