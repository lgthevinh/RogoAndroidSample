package org.thingai.meo.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.thingai.meo.android.app.ui.components.MainBottomNavBar
import org.thingai.meo.android.app.navigations.AppNavGraph
import org.thingai.meo.android.app.ui.components.MainTopAppBar
import rogo.iot.module.platform.ILogR
import rogo.iot.module.rogocore.app.AndroidIoTPlatform
import rogo.iot.module.rogocore.sdk.SmartSdk

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SmartSdk.isForceProduction = false
        AndroidIoTPlatform(this, false)
        // ThingEdges
        SmartSdk().initV2(
            "1222716f697c4bd29a109277c5f977b8",
            "07d67b516eec64a610e7522f59c45dbc5fa8b70fe72f"
        )

        // ThingEdges Staging
//         SmartSdk().initV2(
//             "f07b9dc8912e44ed8b4c6e895acd02c2",
//             "731eee3c8c8ca3de3b178264b7a6a13e80d42f1f1bc1"
//         )

         // Rogo Staging
//        SmartSdk().initV2(
//            "f78f5dd2fc594475a27bef7c2caf9ab4",
//            "41d96be770b2902f801b1689c5edae29c16a068e8f87"
//        )


        // Rogo
//        SmartSdk().initV2(
//            "e4b75a6b23fc4f30bd5fab35436c6a90",
//            "964e2c974f001a0468bf2734ce88e96652afff328886"
//        )
        ILogR.setEnablePrint(true)
        ILogR.D("MainApplication", "onCreate", "SDK Init")

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentRoute by navController.currentBackStackEntryAsState()
            Scaffold(
                topBar = {
                    MainTopAppBar(currentRoute?.destination?.route, navController)
                },
                bottomBar = { MainBottomNavBar(currentRoute?.destination?.route, navController) }
            ) { innerPadding ->
                AppNavGraph(navController = navController, modifier = Modifier.padding(paddingValues = innerPadding))
            }
        }
    }
}