package com.example.hobbyhub.core.utils.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Needed for AuthRepository instance if not using Hilt ViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.data.AuthRepository // Import AuthRepository
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.example.hobbyhub.ui.theme.EventHubPrimary
import com.google.firebase.auth.ktx.auth // Direct Firebase Auth access
import com.google.firebase.ktx.Firebase // Direct Firebase access
import kotlinx.coroutines.launch // Import launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideAppBar(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
    // Consider injecting AuthRepository via Hilt or passing it if needed in a ViewModel
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val upgradeProColor = Color(0xFF00C853) // A vibrant green for the upgrade button
    val scope = rememberCoroutineScope() // Coroutine scope for navigation

    // Get AuthRepository instance (You might inject this differently with Hilt)
    val authRepository = remember { AuthRepository(Firebase.auth) }


    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp), // A standard width for drawers
        drawerContainerColor = Color.White,
        drawerContentColor = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            // Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {
                if (userProfile.profileImageUri != null) {
                    AsyncImage(
                        model = userProfile.profileImageUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Replace with actual user image
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(userProfile.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
            }

            // Navigation Items
            DrawerMenuItem(icon = Icons.Outlined.Person, text = "My Profile", onClick = { navController.navigate(Screen.ProfileScreen.route)})
            DrawerMenuItem(icon = Icons.Outlined.Message, text = "Message", hasBadge = true, onClick = { navController.navigate(Screen.MessageScreen.route)})
            DrawerMenuItem(icon = Icons.Outlined.CalendarToday, text = "Calender", onClick = { navController.navigate(Screen.CalendarScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.BookmarkBorder, text = "Bookmark", onClick = { navController.navigate(Screen.BookmarkScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.Email, text = "Contact Us", onClick = { navController.navigate(Screen.ContactUsScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.Settings, text = "Settings", onClick = { navController.navigate(Screen.SettingsScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.HelpOutline, text = "Helps & FAQs", onClick = { navController.navigate(Screen.HelpScreen.route) })

            Spacer(modifier = Modifier.weight(1f))

            // Sign Out and Delete Account Section
            Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            // *** Sign Out Logic Added ***
            DrawerMenuItem(
                icon = Icons.Outlined.Logout,
                text = "Sign Out",
                onClick = {
                    authRepository.signOut()
                    // Navigate back to Login, clearing the back stack
                    scope.launch {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true // Avoid multiple copies of login screen
                        }
                    }
                }
            )
            // ***************************

            DrawerMenuItem(
                icon = Icons.Outlined.Delete,
                text = "Delete Account",
                onClick = { navController.navigate(Screen.DeleteAccountScreen.route) },
                contentColor = Color.Red
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle Upgrade Pro click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = upgradeProColor.copy(alpha = 0.1f)),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = "Upgrade Pro",
                    tint = upgradeProColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upgrade Pro", color = upgradeProColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// DrawerMenuItem composable remains the same
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    hasBadge: Boolean = false,
    contentColor: Color = Color.DarkGray,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = contentColor,
            fontSize = 16.sp
        )
        if (hasBadge) {
            Badge(
                containerColor = EventHubPrimary
            ) {
                Text(text = "3", color = Color.White)
            }
        }
    }
}