package com.example.hobbyhub.mainui.onboardingscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.hobbyhub.core.navigations.Screen

@Composable
fun OnboardingScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to HobbyHub!")
        // You can add more onboarding UI elements here

        Button(onClick = {
            navController.popBackStack() // Remove onboarding from the back stack
            navController.navigate(Screen.LoginScreen.route)
        }) {
            Text(text = "Get Started")
        }
    }
}