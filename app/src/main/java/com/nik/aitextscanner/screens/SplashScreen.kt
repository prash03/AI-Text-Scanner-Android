
package com.nik.aitextscanner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nik.aitextscanner.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(nav: NavController) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        delay(2000)
        nav.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        // Using AsyncImage from Coil to handle adaptive icons/mipmaps safely
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.mipmap.ic_launcher)
                .build(),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
    }
}
