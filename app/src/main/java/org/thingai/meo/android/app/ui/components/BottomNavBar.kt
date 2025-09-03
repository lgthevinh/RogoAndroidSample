package org.thingai.meo.android.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun MainBottomNavBar(currentRoute: String?, navController: NavController) {
    when (currentRoute) {
        "splash", "login", "signup", "forgot_password", "location",
        "addwile" -> return
    }
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("devices", Icons.Default.Devices, "Devices"),
        BottomNavItem("setting", Icons.Default.Settings, "Setting")
    )
    BottomAppBar(items, navController)
}

@Composable
fun BottomAppBar(bottomAppBarItems: List<BottomNavItem>, navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        bottomAppBarItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview
@Composable
fun MainBottomNavBarPreview() {
    val dummyNavController = rememberNavController()
    MainBottomNavBar("home" ,navController = dummyNavController)
}