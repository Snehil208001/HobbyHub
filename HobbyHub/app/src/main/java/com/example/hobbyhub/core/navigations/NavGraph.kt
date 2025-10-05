package com.example.hobbyhub.core.navigations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hobbyhub.mainui.onboardingscreen.ui.OnboardingScreen
import com.example.hobbyhub.mainui.splashscreen.ui.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.OnboardingScreen.route) { // Add this composable
            OnboardingScreen(navController = navController)
        }

    }
}

// ... (HomeScreen, LoginScreen, SignupScreen placeholders)