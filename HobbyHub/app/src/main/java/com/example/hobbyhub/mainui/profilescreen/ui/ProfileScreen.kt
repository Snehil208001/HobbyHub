// In snehil208001/hobbyhub/HobbyHub-102bbb5bfeae283b4c3e52ca5e13f3198e956095/HobbyHub/app/src/main/java/com/example/hobbyhub/mainui/profilescreen/ui/ProfileScreen.kt

package com.example.hobbyhub.mainui.profilescreen.ui

import android.text.format.DateFormat
import android.util.Log // <-- Added missing import
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.* import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Graph
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.google.accompanist.flowlayout.FlowRow
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.EditProfile.route)
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading && user == null) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            } else if (uiState.error != null && user == null) {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (user != null) {
                // --- Profile Header ---
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.profileImageUrl.ifEmpty { R.drawable.logo })
                        .crossfade(true)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // --- Info Row (Location, Website, Joined Date, Privacy) ---
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (user.location.isNotBlank()) {
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            text = user.location
                        )
                    }

                    if (user.website.isNotBlank()) {
                        val url = if (user.website.startsWith("http://") || user.website.startsWith("https://")) {
                            user.website
                        } else {
                            "https://${user.website}"
                        }
                        InfoRow(
                            icon = Icons.Default.Language,
                            text = user.website.removePrefix("https://").removePrefix("http://"),
                            color = MaterialTheme.colorScheme.primary,
                            onClick = {
                                try {
                                    uriHandler.openUri(url)
                                } catch (e: Exception) {
                                    Log.e("ProfileScreen", "Failed to open URI", e)
                                }
                            }
                        )
                    }

                    if (user.joinedDate != 0L) {
                        InfoRow(
                            icon = Icons.Default.CalendarMonth,
                            text = "Joined ${formatJoinedDate(user.joinedDate)}"
                        )
                    }

                    InfoRow(
                        icon = if (user.isPrivate) Icons.Default.Lock else Icons.Default.Visibility,
                        text = if (user.isPrivate) "Private Profile" else "Public Profile"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                ProfileStatsRow(hobbyCount = user.hobbies.size, groupCount = 0, eventCount = 0)

                // Bio Section
                if (user.bio.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            SectionTitle("About Me")
                            Text(
                                text = user.bio,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            SectionTitle("About Me")
                            Text(
                                text = "No bio yet. Tap edit to add one!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Hobbies Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle("My Hobbies & Interests")
                        val hobbies = user.hobbies
                        if (hobbies.isEmpty()) {
                            Text(
                                text = "No hobbies added yet. Tap edit to add some!",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Start
                            )
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                mainAxisSpacing = 8.dp,
                                crossAxisSpacing = 8.dp
                            ) {
                                hobbies.forEach { hobby ->
                                    SuggestionChip(
                                        onClick = { /* Maybe navigate to hobby detail? */ },
                                        label = { Text(hobby) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Out Button
                Button(
                    onClick = {
                        viewModel.onSignOutClick {
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(Graph.ROOT) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Sign Out")
                }

                Spacer(modifier = Modifier.height(16.dp))

            } else {
                if (!uiState.isLoading) {
                    Text(
                        "User profile not found.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                }
            }
        }
    }
}

// Helper composable for section titles
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp),
        textAlign = TextAlign.Start
    )
}

// Composable for Stats Row
@Composable
fun ProfileStatsRow(hobbyCount: Int, groupCount: Int, eventCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(icon = Icons.Default.Interests, label = "Hobbies", count = hobbyCount)
        StatDivider()
        StatItem(icon = Icons.Default.Group, label = "Groups", count = groupCount)
        StatDivider()
        StatItem(icon = Icons.Default.Event, label = "Events", count = eventCount)
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StatDivider() {
    Divider(
        modifier = Modifier
            .height(50.dp)
            .width(1.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
}

// Helper composable for info rows
@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(16.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

// Helper function to format the timestamp
private fun formatJoinedDate(timestamp: Long): String {
    try {
        val date = Date(timestamp)
        // --- THIS IS THE FIX ---
        // We use the full path to avoid import confusion with Compose Color
        return android.text.format.DateFormat.format("MMMM yyyy", date).toString()
        // --- END FIX ---
    } catch (e: Exception) {
        return "N/A"
    }
}