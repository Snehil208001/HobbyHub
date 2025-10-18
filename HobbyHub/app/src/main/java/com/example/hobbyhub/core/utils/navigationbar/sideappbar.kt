package com.example.hobbyhub.core.utils.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.example.hobbyhub.ui.theme.EventHubPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideAppBar(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val upgradeProColor = Color(0xFF00C853)
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp),
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
                // --- CORRECTED IMAGE LOGIC ---
                // We only show the database URL or the logo, not the local URI
                if (userProfile.databaseImageUrl.isNotBlank()) {
                    AsyncImage(
                        model = userProfile.databaseImageUrl, // Use the String URL
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Default logo
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }
                // --- END CORRECTION ---

                Spacer(modifier = Modifier.width(16.dp))

                // This logic is correct
                if (userProfile.name.isBlank()) {
                    Text("Loading...", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                } else {
                    Text(userProfile.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                }
            }

            // Navigation Items (Unchanged)
            DrawerMenuItem(icon = Icons.Outlined.Person, text = "My Profile", onClick = { navController.navigate(Screen.ProfileScreen.route)})
            DrawerMenuItem(icon = Icons.Outlined.Message, text = "Message", hasBadge = true, onClick = { navController.navigate(Screen.MessageScreen.route)})
            DrawerMenuItem(icon = Icons.Outlined.CalendarToday, text = "Calender", onClick = { navController.navigate(Screen.CalendarScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.BookmarkBorder, text = "Bookmark", onClick = { navController.navigate(Screen.BookmarkScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.Email, text = "Contact Us", onClick = { navController.navigate(Screen.ContactUsScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.Settings, text = "Settings", onClick = { navController.navigate(Screen.SettingsScreen.route) })
            DrawerMenuItem(icon = Icons.Outlined.HelpOutline, text = "Helps & FAQs", onClick = { navController.navigate(Screen.HelpScreen.route) })

            Spacer(modifier = Modifier.weight(1f))

            // Sign Out and Delete Account Section (Unchanged)
            Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
            DrawerMenuItem(
                icon = Icons.Outlined.Logout,
                text = "Sign Out",
                onClick = {
                    profileViewModel.signOut()
                    scope.launch {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
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

// DrawerMenuItem composable (Unchanged)
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