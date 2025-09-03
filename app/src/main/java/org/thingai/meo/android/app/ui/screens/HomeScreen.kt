package org.thingai.meo.android.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import rogo.iot.module.rogocore.sdk.SmartSdk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    if (SmartSdk.getAppLocation() == null) {
        // Redirect to location setup if not set
        navController.navigate("location")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Meo Android!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("location") }) {
            Text("View Locations")
        }
        // Extend this with more IoT data, actions, etc.
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val dummyNavController = androidx.navigation.compose.rememberNavController()
    HomeScreen(navController = dummyNavController)
}