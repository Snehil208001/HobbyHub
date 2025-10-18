package com.example.hobbyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.hobbyhub.core.navigations.Screen // Import Screen
import com.example.hobbyhub.core.navigations.SetupNavGraph
import com.example.hobbyhub.data.AuthRepository // Import AuthRepository
import com.example.hobbyhub.ui.theme.HobbyHubTheme
import com.google.firebase.auth.FirebaseAuth // Import FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject // Import Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject // Inject FirebaseAuth or AuthRepository
    lateinit var auth: FirebaseAuth // Or lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Determine the start destination based on login state
        // val currentUser = authRepository.getCurrentUser() // Use this if injecting AuthRepository
        val currentUser = auth.currentUser // Use this if injecting FirebaseAuth
        val startDestination = if (currentUser != null) {
            Screen.HomeScreen.route // User is logged in
        } else {
            Screen.SplashScreen.route // User is not logged in, start with splash/onboarding/login flow
            // Alternatively, start directly at LoginScreen if splash/onboarding is only for the very first launch
            // Screen.LoginScreen.route
        }


        setContent {
            HobbyHubTheme {
                val navController = rememberNavController()
                // Pass the determined startDestination to SetupNavGraph
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}