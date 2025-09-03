package org.thingai.meo.android.app.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.thingai.meo.android.app.ui.screens.DevicesScreen
import org.thingai.meo.android.app.ui.screens.auth.ForgotPasswordScreen
import org.thingai.meo.android.app.ui.screens.HomeScreen
import org.thingai.meo.android.app.ui.screens.LocationsScreen
import org.thingai.meo.android.app.ui.screens.auth.LoginScreen
import org.thingai.meo.android.app.ui.screens.SettingScreen
import org.thingai.meo.android.app.ui.screens.auth.SignUpScreen
import org.thingai.meo.android.app.ui.screens.SplashScreen
import org.thingai.meo.android.app.ui.screens.wile.ScanWileScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController) }
        composable("location") { LocationsScreen(navController) }
        composable("devices") { DevicesScreen(navController) }
        composable("setting") { SettingScreen(navController) }

        composable("addwile") { ScanWileScreen(navController = navController) }
    }
}