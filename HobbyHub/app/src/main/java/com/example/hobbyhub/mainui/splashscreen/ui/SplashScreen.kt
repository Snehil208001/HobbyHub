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
import androidx.hilt.navigation.compose.hiltViewModel // ADDED
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.splashscreen.viewmodel.SplashScreenEvent // ADDED
import com.example.hobbyhub.mainui.splashscreen.viewmodel.SplashScreenViewModel // ADDED
import kotlinx.coroutines.delay // Removed (but kept the import for safe removal)

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel() // INJECTED VIEWMODEL
) {
    // Collect the state from the ViewModel
    val startAnimation by viewModel.startAnimation.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    // Animation logic remains in the Composable for UI-related state management
    val scale = animateFloatAsState(
        // Use the state from the ViewModel to drive the animation
        targetValue = if (startAnimation) 1.2f else 0.3f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 100
        ),
        label = "scaleAnimation"
    )

    // Side effect to handle navigation triggered by the ViewModel
    LaunchedEffect(key1 = navigationEvent) {
        if (navigationEvent is SplashScreenEvent.NavigateToOnboarding) {
            navController.popBackStack()
            navController.navigate(Screen.OnboardingScreen.route)
        }
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