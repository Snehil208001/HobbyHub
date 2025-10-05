package com.example.hobbyhub.core.navigations

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object OnboardingScreen : Screen("onboarding_screen") // Add this line
    object HomeScreen : Screen("home_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
}