
package com.nik.aitextscanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.nik.aitextscanner.screens.*

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") { SplashScreen(nav) }
        composable("home") { HomeScreen(nav) }
        composable("camera") { CameraScreen(nav) }
        composable("result/{text}") {
            ResultScreen(nav, it.arguments?.getString("text") ?: "")
        }
    }
}
