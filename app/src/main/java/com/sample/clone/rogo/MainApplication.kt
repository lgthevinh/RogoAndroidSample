package com.sample.clone.rogo

import android.app.Application
import rogo.iot.module.platform.ILogR
import rogo.iot.module.rogocore.app.AndroidIoTPlatform
import rogo.iot.module.rogocore.sdk.SmartSdk

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Staging environment
        SmartSdk.isForceProduction = false

        AndroidIoTPlatform(this, false)
        // Rogo
//        SmartSdk().initV2(
//            "e4b75a6b23fc4f30bd5fab35436c6a90",
//            "964e2c974f001a0468bf2734ce88e96652afff328886"
//        )

        // ThingEdges Staging
//        SmartSdk().initV2(
//            "f07b9dc8912e44ed8b4c6e895acd02c2",
//            "731eee3c8c8ca3de3b178264b7a6a13e80d42f1f1bc1"
//        )

//        // Rogo Staging
//        SmartSdk().initV2(
//            "f78f5dd2fc594475a27bef7c2caf9ab4",
//            "41d96be770b2902f801b1689c5edae29c16a068e8f87"
//        )

        // ThingEdges
        SmartSdk().initV2(
            "1222716f697c4bd29a109277c5f977b8",
            "07d67b516eec64a610e7522f59c45dbc5fa8b70fe72f"
        )
        ILogR.setEnablePrint(true)
        ILogR.D("MainApplication", "onCreate", "SDK Init")
    }
}