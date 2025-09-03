package org.thingai.meo.android.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class DeviceTypeItem (
    val label: String,
    val route: String,
)

@Composable
fun DevicesScreen(navController: NavController) {
    val deviceTypes: Collection<DeviceTypeItem> = listOf(
        DeviceTypeItem("Wile", "addwile"),
        DeviceTypeItem("BLE Mesh", "addblesmesh"),
        DeviceTypeItem("Zigbee", "addzigbee"),
        DeviceTypeItem("Gateway", "addgateway"),
        DeviceTypeItem("IR Remote", "addirremote"),
        DeviceTypeItem("MEO Device", "addmeodevice"),
    )
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Add devices",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(deviceTypes.size) { index ->
                val deviceType = deviceTypes.elementAt(index)
                DeviceTypeCard(deviceType = deviceType) {
                    navController.navigate(deviceType.route)
                }
            }
        }


    }
    
}

@Composable
fun DeviceTypeCard(
    deviceType: DeviceTypeItem,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = deviceType.label,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun DevicesScreenPreview() {
    DevicesScreen(navController = rememberNavController())
}