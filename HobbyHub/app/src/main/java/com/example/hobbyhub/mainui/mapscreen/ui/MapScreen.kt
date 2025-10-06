package com.example.hobbyhub.mainui.mapscreen.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
// Using Material 3 components
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hobbyhub.R
import com.example.hobbyhub.mainui.mapscreen.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val mapState by viewModel.mapState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        // Initial position set to a default for testing/first load (e.g., NYC)
        position = CameraPosition.fromLatLngZoom(LatLng(40.7128, -74.0060), 12f)
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Load the custom map style from res/raw/map_style.json
    // NOTE: Ensure you have a 'map_style.json' file in your 'res/raw/' directory.
    val mapStyle = remember { MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = false,
                mapStyleOptions = mapStyle // Apply the custom style
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomGesturesEnabled = true,
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.getDeviceLocation(context)
            }
        }
    )

    // Launch permission request when the screen is first composed
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Animate camera to the user's last known location when it becomes available
    LaunchedEffect(mapState.lastKnownLocation) {
        mapState.lastKnownLocation?.let {
            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(it, 15f),
                    durationMs = 1000
                )
            }
        }
    }

    // A deep purple color to match the app's theme
    val appPrimaryColor = Color(0xFF673AB7)

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            // Clear the selected location when the user clicks anywhere on the map
            onMapClick = { viewModel.clearSelectedLocation() }
        ) {
            mapState.hobbyLocations.forEach { hobby ->
                Marker(
                    state = MarkerState(position = hobby.location),
                    title = hobby.name,
                    snippet = hobby.description,
                    // Use different colored default markers based on category
                    icon = BitmapDescriptorFactory.defaultMarker(
                        when (hobby.category) {
                            "Sports" -> BitmapDescriptorFactory.HUE_ORANGE
                            "Music" -> BitmapDescriptorFactory.HUE_BLUE
                            "Art" -> BitmapDescriptorFactory.HUE_GREEN
                            "Photography" -> BitmapDescriptorFactory.HUE_YELLOW
                            "Games" -> BitmapDescriptorFactory.HUE_MAGENTA
                            else -> BitmapDescriptorFactory.HUE_RED
                        }
                    ),
                    onClick = {
                        viewModel.onMarkerClick(hobby)
                        false // Return false to show the default info window on click
                    }
                )
            }
        }

        // 2. Custom Search Bar UI (Matches the overall app theme)
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            // FIX: Use CardDefaults.cardElevation for Material 3 Card
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White) // M3 way to set background color
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = appPrimaryColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Search for hobbies...",
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                // Filters button styled like the one in the main screen image
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(appPrimaryColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Filters",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // 3. Location Detail Card (Appears when a marker is clicked)
        mapState.selectedLocation?.let { location ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    // Move up above the bottom navigation bar
                    .padding(bottom = 90.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                // FIX: Use CardDefaults.cardElevation for Material 3 Card
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White) // M3 way to set background color
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = location.name,
                            // M3 typography equivalent for h6 is headlineSmall
                            style = MaterialTheme.typography.headlineSmall,
                            color = appPrimaryColor
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close details",
                            tint = Color.Gray,
                            modifier = Modifier.clickable { viewModel.clearSelectedLocation() }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Category: ${location.category}",
                        // M3 typography equivalent for subtitle2 is labelMedium
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = location.description,
                        // M3 typography equivalent for body2 is bodyMedium
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}