package com.example.hobbyhub.mainui.homescreen.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hobbyhub.R
import com.example.hobbyhub.core.utils.navigationbar.BottomNavigationBar
import com.example.hobbyhub.core.utils.navigationbar.HomeTopAppBar
import com.example.hobbyhub.core.utils.navigationbar.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val location by locationViewModel.location.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationViewModel.fetchLocation(context)
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold(
        topBar = { HomeTopAppBar(location = location) },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CategoryFilters()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader("Upcoming Events")
                Spacer(modifier = Modifier.height(16.dp))
                EventsRow()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                InviteFriendsBanner()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CategoryFilters() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Chip(
                text = "Sports",
                icon = Icons.Default.SportsBasketball,
                backgroundColor = Color(0xFFF94336)
            )
        }
        item {
            Chip(
                text = "Music",
                icon = Icons.Default.MusicNote,
                backgroundColor = Color(0xFFFF9800)
            )
        }
        item {
            Chip(
                text = "Food",
                icon = Icons.Default.Restaurant,
                backgroundColor = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun Chip(text: String, icon: ImageVector, backgroundColor: Color) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("See All", color = Color.Gray)
    }
}

@Composable
fun EventsRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item { EventCard() }
        item { EventCard() }
    }
}

@Composable
fun EventCard() {
    Card(
        modifier = Modifier.width(250.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.group3),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text("International Band Mu..", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("+20 Going", color = Color.Gray)
            }
        }
    }
}

@Composable
fun InviteFriendsBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Invite your friends", fontWeight = FontWeight.Bold)
                Text("Get \$20 for ticket", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle Invite */ }) {
                    Text("INVITE")
                }
            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Gift",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}