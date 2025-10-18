package com.example.hobbyhub.mainui.profilescreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hobbyhub.R // Make sure your R file is imported
// Ensure these imports point to YOUR navigation files
import com.example.hobbyhub.core.navigations.Graph // <-- Import Graph
import com.example.hobbyhub.core.navigations.Screen // <-- Import Screen
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.google.accompanist.flowlayout.FlowRow // Add this dependency: implementation "com.google.accompanist.flowlayout:accompanist-flowlayout:0.xx.x"

@OptIn(ExperimentalMaterial3Api::class) // Needed for Chip
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user // Get the user object from the state
    val context = LocalContext.current

    // --- ADDED: Refresh profile when screen becomes visible ---
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }
    // --- END ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = {
                        // --- Use Screen ---
                        navController.navigate(Screen.EditProfile.route)
                        // --- End Use Screen ---
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make content scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading && user == null) { // Show loading only if user is not yet loaded
                CircularProgressIndicator()
            } else if (uiState.error != null && user == null) { // Show error only if loading failed initially
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else if (user != null) {
                // Profile Picture
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.profileImageUrl.ifEmpty { R.drawable.logo }) // Use placeholder if empty
                        .crossfade(true)
                        .error(R.drawable.logo) // Fallback placeholder
                        .placeholder(R.drawable.logo)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Full Name
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Email
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bio Section (Use user.bio directly for display)
                SectionTitle("About Me")
                Text(
                    text = user.bio.ifEmpty { "No bio yet. Tap edit to add one!" },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start // Align bio text to the start
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Hobbies Section (Use user.hobbies directly for display)
                SectionTitle("My Hobbies & Interests")
                val hobbies = user.hobbies // Get hobbies from user state
                if (hobbies.isEmpty()) {
                    Text(
                        text = "No hobbies added yet. Tap edit to add some!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                } else {
                    // Use FlowRow to wrap chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        hobbies.forEach { hobby -> // Iterate user.hobbies
                            SuggestionChip( // Or use FilterChip if selectable
                                onClick = { /* Maybe navigate to hobby detail? */ },
                                label = { Text(hobby) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Push button to bottom

                // Sign Out Button
                Button(
                    onClick = {
                        viewModel.onSignOutClick {
                            // --- Use Screen ---
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(Graph.ROOT) { inclusive = true } // Clear entire backstack using Graph
                                launchSingleTop = true
                            }
                            // --- End Use Screen ---
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }

            } else {
                // This case should ideally not be reached if logged in,
                // but kept as a fallback. Maybe show loading or error.
                if (!uiState.isLoading) { // Avoid showing this text while loading
                    Text(
                        "User profile not found.",
                        textAlign = TextAlign.Center
                    )
                } else {
                    CircularProgressIndicator() // Show loading if user is null but still loading
                }
            }
        }
    }
}

// Helper composable for section titles (keep as is)
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Start
    )
}