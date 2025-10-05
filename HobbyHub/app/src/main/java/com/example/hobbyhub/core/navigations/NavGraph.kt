package com.example.hobbyhub.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hobbyhub.mainui.login.ui.LoginScreen
import com.example.hobbyhub.mainui.login.ui.ResetPasswordScreen // ADDED IMPORT
import com.example.hobbyhub.mainui.onboardingscreen.ui.OnboardingScreen
import com.example.hobbyhub.mainui.signup.ui.SignupScreen
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
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(navController = navController)
        }
        // Add the LoginScreen route
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        // Add the SignupScreen route
        composable(Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }
        // ADDED: ResetPasswordScreen Route
        composable(Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController)
        }
    }
}