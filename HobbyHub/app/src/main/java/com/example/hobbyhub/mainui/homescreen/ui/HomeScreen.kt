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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.hobbyhub.core.utils.navigationbar.SideAppBar
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.hobbyhub.mainui.filterscreen.ui.FilterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val filterSheetState = rememberModalBottomSheetState()
    var showFilterSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = filterSheetState
        ) {
            FilterScreen(
                onApplyClick = {
                    scope.launch { filterSheetState.hide() }.invokeOnCompletion {
                        if (!filterSheetState.isVisible) {
                            showFilterSheet = false
                        }
                    }
                },
                currentLocation = location // Pass the location state here
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideAppBar(navController = navController, profileViewModel = profileViewModel)
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopAppBar(
                    location = location,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onFilterClick = {
                        showFilterSheet = true
                    }
                )
            },
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
                    SectionHeader("Trending Hobbies")
                    Spacer(modifier = Modifier.height(16.dp))
                    HobbiesRow()
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    SectionHeader("Personalized Recommendations")
                    Spacer(modifier = Modifier.height(16.dp))
                    HobbiesRow()
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    FindNewHobbyBanner()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
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
fun HobbiesRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item { HobbyCard() }
        item { HobbyCard() }
    }
}

@Composable
fun HobbyCard() {
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
                Text("Photography", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Capture the world around you.", color = Color.Gray)
            }
        }
    }
}

@Composable
fun FindNewHobbyBanner() {
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
                Text("Find Your Next Hobby", fontWeight = FontWeight.Bold)
                Text("Explore new activities", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle Invite */ }) {
                    Text("EXPLORE")
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