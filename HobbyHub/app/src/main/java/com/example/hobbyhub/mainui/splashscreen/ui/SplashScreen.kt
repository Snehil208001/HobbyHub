package com.example.hobbyhub.mainui.splashscreen.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        // Change the target value to make the image larger
        targetValue = if (startAnimation) 1.2f else 0.3f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 100
        ),
        label = "scaleAnimation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000L) // Wait for 2 seconds
        navController.popBackStack()
        navController.navigate(Screen.OnboardingScreen.route)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.hobbyhubsplash),
            contentDescription = "HobbyHub Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}