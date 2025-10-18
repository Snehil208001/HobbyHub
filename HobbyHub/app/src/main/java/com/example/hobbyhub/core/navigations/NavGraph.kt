package com.example.hobbyhub.core.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hobbyhub.core.utils.navigationbar.LocationViewModel
import com.example.hobbyhub.mainui.bookmarkscreen.ui.BookmarkScreen
import com.example.hobbyhub.mainui.calendarscreen.ui.CalendarScreen
import com.example.hobbyhub.mainui.contactusscreen.ui.ContactUsScreen
import com.example.hobbyhub.mainui.deletescreen.ui.DeleteAccountScreen
import com.example.hobbyhub.mainui.explorescreen.ui.ExploreScreen
import com.example.hobbyhub.mainui.filterscreen.ui.FilterScreen
import com.example.hobbyhub.mainui.groupsscreen.ui.GroupsScreen
import com.example.hobbyhub.mainui.helpscreen.ui.HelpScreen
import com.example.hobbyhub.mainui.homescreen.ui.HomeScreen
import com.example.hobbyhub.mainui.login.ui.LoginScreen
import com.example.hobbyhub.mainui.login.ui.ResetPasswordScreen
import com.example.hobbyhub.mainui.mapscreen.ui.MapScreen
import com.example.hobbyhub.mainui.messagescreen.ui.MessageScreen
import com.example.hobbyhub.mainui.onboardingscreen.ui.OnboardingScreen
import com.example.hobbyhub.mainui.profilescreen.ui.ProfileScreen
import com.example.hobbyhub.mainui.settingsscreen.ui.SettingsScreen
import com.example.hobbyhub.mainui.signup.ui.SignupScreen
import com.example.hobbyhub.mainui.splashscreen.ui.SplashScreen
import com.example.hobbyhub.mainui.workshopsscreen.ui.WorkshopsScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String // <-- ADD THIS PARAMETER
) {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val location by locationViewModel.location.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination // <-- USE THE PARAMETER HERE
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }
//        composable(Screen.ResetPasswordScreen.route) {
//            ResetPasswordScreen(navController = navController)
//        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable(Screen.ExploreScreen.route) {
            ExploreScreen(navController = navController)
        }
        composable(Screen.GroupsScreen.route) {
            GroupsScreen(navController = navController)
        }
        composable(Screen.MapScreen.route) {
            MapScreen(navController = navController)
        }
        composable(Screen.WorkshopsScreen.route) {
            WorkshopsScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Screen.DeleteAccountScreen.route) {
            DeleteAccountScreen(navController = navController)
        }
        composable(Screen.FilterScreen.route) {
            FilterScreen(
                onApplyClick = {
                    navController.popBackStack()
                },
                currentLocation = location
            )
        }
        composable(Screen.MessageScreen.route) {
            MessageScreen(navController = navController)
        }

        composable(Screen.CalendarScreen.route) {
            CalendarScreen(navController = navController)
        }

        composable(Screen.BookmarkScreen.route) {
            BookmarkScreen(navController = navController)
        }

        composable(Screen.ContactUsScreen.route) {
            ContactUsScreen(navController = navController)
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.HelpScreen.route) {
            HelpScreen(navController = navController)
        }

    }
}