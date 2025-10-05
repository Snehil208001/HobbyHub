package com.example.hobbyhub.core.navigations

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object OnboardingScreen : Screen("onboarding_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object ResetPasswordScreen : Screen("reset_password_screen")
    object HomeScreen : Screen("home_screen")
    object ExploreScreen : Screen("explore_screen")
    object GroupsScreen : Screen("groups_screen")
    object MapScreen : Screen("map_screen")
    object WorkshopsScreen : Screen("workshops_screen")
    object ProfileScreen : Screen("profile_screen")
}