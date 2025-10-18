package com.example.hobbyhub.mainui.deletescreen.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // Added for Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hobbyhub.core.navigations.Screen // Added for navigation
import com.example.hobbyhub.data.AuthRepository // Added
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException // Added
import com.google.firebase.auth.ktx.auth // Added
import com.google.firebase.ktx.Firebase // Added
import kotlinx.coroutines.launch // Added

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(navController: NavController) {
    var confirmationText by remember { mutableStateOf("") }
    val isButtonEnabled = confirmationText.equals("DELETE", ignoreCase = true) // Case-insensitive check
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Get AuthRepository instance (You might inject this differently with Hilt)
    val authRepository = remember { AuthRepository(Firebase.auth) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delete Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // Changed arrangement to start to prevent centering everything vertically
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Add some space at the top
            Text(
                "Are you sure you want to delete your account?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Center text
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "This action is irreversible. All your data will be permanently deleted.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Center text
            )
            Spacer(modifier = Modifier.height(24.dp)) // Increased space
            OutlinedTextField(
                value = confirmationText,
                onValueChange = { confirmationText = it },
                label = { Text("Type 'DELETE' to confirm") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp)) // Increased space
            Button(
                onClick = {
                    scope.launch {
                        val user = authRepository.getCurrentUser()
                        if (user != null) {
                            try {
                                user.delete().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
                                        // Navigate back to Login, clearing the stack
                                        navController.navigate(Screen.LoginScreen.route) {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        // Handle specific errors like needing recent login
                                        val exception = task.exception
                                        if (exception is FirebaseAuthRecentLoginRequiredException) {
                                            Toast.makeText(context, "Please sign out and sign back in to delete your account.", Toast.LENGTH_LONG).show()
                                            // Optionally, sign the user out here
                                            authRepository.signOut()
                                            navController.navigate(Screen.LoginScreen.route) {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            Toast.makeText(context, "Failed to delete account: ${exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            } catch (e: Exception) { // Catch other potential exceptions during delete call setup
                                Toast.makeText(context, "Error initiating account deletion: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "No user logged in.", Toast.LENGTH_SHORT).show()
                            // Navigate to login if no user is found
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    disabledContainerColor = Color.Red.copy(alpha = 0.5f) // Indicate disabled state visually
                ),
                modifier = Modifier.fillMaxWidth() // Make button full width
            ) {
                Text("DELETE MY ACCOUNT PERMANENTLY", color = Color.White) // Clearer text
            }
        }
    }
}