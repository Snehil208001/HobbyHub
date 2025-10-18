package com.example.hobbyhub.core.navigations

// Keep this Graph object if you use it for popUpTo logic
object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph" // Optional grouping
    const val MAIN = "main_graph"          // Optional grouping
}


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
    object DeleteAccountScreen : Screen("delete_account_screen")
    object FilterScreen : Screen("filter_screen")
    object MessageScreen : Screen("message_screen")
    object CalendarScreen : Screen("calendar_screen")
    object BookmarkScreen : Screen("bookmark_screen")
    object ContactUsScreen : Screen("contact_us_screen")
    object SettingsScreen : Screen("settings_screen")
    object HelpScreen : Screen("help_screen") // Keep this line
    object EditProfile : Screen("edit_profile") // Keep this line
}