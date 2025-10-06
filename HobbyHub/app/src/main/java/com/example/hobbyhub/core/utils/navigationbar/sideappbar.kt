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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideAppBar(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val upgradeProColor = Color(0xFF00C853) // A vibrant green for the upgrade button

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
            DrawerMenuItem(icon = Icons.Outlined.Person, text = "My Profile")
            DrawerMenuItem(icon = Icons.Outlined.Message, text = "Message", hasBadge = true)
            DrawerMenuItem(icon = Icons.Outlined.CalendarToday, text = "Calender")
            DrawerMenuItem(icon = Icons.Outlined.BookmarkBorder, text = "Bookmark")
            DrawerMenuItem(icon = Icons.Outlined.Email, text = "Contact Us")
            DrawerMenuItem(icon = Icons.Outlined.Settings, text = "Settings")
            DrawerMenuItem(icon = Icons.Outlined.HelpOutline, text = "Helps & FAQs")

            Spacer(modifier = Modifier.weight(1f))

            // Sign Out and Upgrade Section
            Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
            DrawerMenuItem(icon = Icons.Outlined.Logout, text = "Sign Out")
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