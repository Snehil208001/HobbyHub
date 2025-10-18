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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel // Added
import androidx.navigation.NavController
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.deletescreen.viewmodel.DeleteAccountViewModel // Added

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    navController: NavController,
    viewModel: DeleteAccountViewModel = hiltViewModel() // Injected ViewModel
) {
    var confirmationText by remember { mutableStateOf("") }
    val isButtonEnabled = confirmationText.equals("DELETE", ignoreCase = true)
    val context = LocalContext.current

    // Get state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // --- Observe ViewModel State ---
    LaunchedEffect(uiState) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
            // Navigate back to Login, clearing the stack
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            viewModel.resetState() // Reset state after navigation
        }

        if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()

            if (uiState.requiresRecentLogin) {
                // If recent login is required, sign out and force navigation to login
                viewModel.signOut()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
            viewModel.resetState() // Reset state after showing error
        }
    }
    // --- End Observe ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delete Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Corrected: popBackStack()
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
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Are you sure you want to delete your account?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "This action is irreversible. All your data will be permanently deleted.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = confirmationText,
                onValueChange = { confirmationText = it },
                label = { Text("Type 'DELETE' to confirm") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        // Call ViewModel function
                        viewModel.deleteAccount()
                    },
                    // Disable button if text doesn't match OR if loading
                    enabled = isButtonEnabled && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        disabledContainerColor = Color.Red.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DELETE MY ACCOUNT PERMANENTLY", color = Color.White)
                }

                // Show loading indicator
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}