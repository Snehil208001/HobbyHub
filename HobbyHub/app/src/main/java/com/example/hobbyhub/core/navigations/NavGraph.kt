package com.example.hobbyhub.core.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hobbyhub.core.utils.navigationbar.LocationViewModel
import com.example.hobbyhub.mainui.deletescreen.ui.DeleteAccountScreen
import com.example.hobbyhub.mainui.explorescreen.ui.ExploreScreen
import com.example.hobbyhub.mainui.filterscreen.ui.FilterScreen
import com.example.hobbyhub.mainui.groupsscreen.ui.GroupsScreen
import com.example.hobbyhub.mainui.homescreen.ui.HomeScreen
import com.example.hobbyhub.mainui.login.ui.LoginScreen
import com.example.hobbyhub.mainui.login.ui.ResetPasswordScreen
import com.example.hobbyhub.mainui.mapscreen.ui.MapScreen
import com.example.hobbyhub.mainui.onboardingscreen.ui.OnboardingScreen
import com.example.hobbyhub.mainui.profilescreen.ui.ProfileScreen
import com.example.hobbyhub.mainui.signup.ui.SignupScreen
import com.example.hobbyhub.mainui.splashscreen.ui.SplashScreen
import com.example.hobbyhub.mainui.workshopsscreen.ui.WorkshopsScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val location by locationViewModel.location.collectAsState()

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
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable(Screen.ExploreScreen.route) {
            ExploreScreen()
        }
        composable(Screen.GroupsScreen.route) {
            GroupsScreen()
        }
        composable(Screen.MapScreen.route) {
            MapScreen(navController = navController)
        }
        composable(Screen.WorkshopsScreen.route) {
            WorkshopsScreen()
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
    }
}