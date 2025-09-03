package org.thingai.meo.android.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import rogo.iot.module.rogocore.sdk.SmartSdk


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    currentRoute: String?, navController: NavController,
) {
    when (currentRoute) {
        "home" -> AppDefaultTopAppBar("Home", navController)
        "devices" -> AppDefaultTopAppBar("Devices", navController)
        "setting" -> AppDefaultTopAppBar("Settings", navController)
        "location" -> AppDefaultTopAppBar("Select Location", navController)
        "addwile" -> AppDefaultTopAppBar("Add Wile Devices", navController, onBackClick = {
            SmartSdk.discoverySmartDeviceHandler().stopDiscovery()
        })

        "splash", "login", "signup", "forgot_password" -> {}
        else -> AppDefaultTopAppBar("Meo Service", navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDefaultTopAppBar(title: String, navController: NavController, onBackClick: () -> Unit = {}) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                title,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onBackClick()
                        navController.popBackStack()
                    }
            )
        }
    )
}

@Preview
@Composable
fun MainTopAppBarPreview() {
    val dummyNavController = androidx.navigation.compose.rememberNavController()
    MainTopAppBar("home", dummyNavController)
}