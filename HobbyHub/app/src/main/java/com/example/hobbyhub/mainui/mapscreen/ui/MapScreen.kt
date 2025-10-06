package com.example.hobbyhub.mainui.mapscreen.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hobbyhub.R
import com.example.hobbyhub.core.utils.composableToBitmapDescriptor
import com.example.hobbyhub.mainui.mapscreen.viewmodel.HobbyLocation
import com.example.hobbyhub.mainui.mapscreen.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val mapState by viewModel.mapState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.7128, -74.0060), 12f)
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val parentComposition = rememberCompositionContext()

    // State for the search input field
    var searchText by remember { mutableStateOf("") }


    // Load the custom map style
    val mapStyle = remember { MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = false,
                mapStyleOptions = mapStyle
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomGesturesEnabled = true,
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
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

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

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

    LaunchedEffect(mapState.selectedLocation) {
        if (mapState.selectedLocation != null) {
            coroutineScope.launch {
                bottomSheetState.expand()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 150.dp,
        sheetContent = {
            mapState.selectedLocation?.let { location ->
                HobbyLocationDetails(location)
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a location on the map to see details.")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings
            ) {
                mapState.hobbyLocations.forEach { hobby ->
                    var bitmapDescriptor by remember { mutableStateOf<BitmapDescriptor?>(null) }
                    LaunchedEffect(hobby.category) {
                        bitmapDescriptor = composableToBitmapDescriptor(context, parentComposition) {
                            CustomMapMarker(hobby = hobby)
                        }
                    }

                    bitmapDescriptor?.let {
                        Marker(
                            state = MarkerState(position = hobby.location),
                            title = hobby.name,
                            snippet = hobby.description,
                            icon = it,
                            onClick = {
                                viewModel.onMarkerClick(hobby)
                                false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                SearchBar(
                    inputField = searchText,
                    onValueChange = { searchText = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CategoryFilters()
            }

            FloatingActionButton(
                onClick = {
                    mapState.lastKnownLocation?.let {
                        coroutineScope.launch {
                            cameraPositionState.animate(update = CameraUpdateFactory.newLatLngZoom(it, 15f))
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 16.dp, end = 16.dp)
                    .size(48.dp),
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Composable
fun CustomMapMarker(hobby: HobbyLocation) {
    val markerColor = when (hobby.category) {
        "Sports" -> Color(0xFFF44336) // Red
        "Music" -> Color(0xFF2196F3) // Blue
        "Art" -> Color(0xFF4CAF50) // Green
        "Photography" -> Color(0xFFFF9800) // Orange
        else -> Color(0xFF9C27B0) // Purple
    }
    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = markerColor)
    ) {
        Icon(
            imageVector = hobby.icon,
            contentDescription = hobby.name,
            tint = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        )
    }
}


@Composable
private fun SearchBar(inputField: String, onValueChange: (String) -> Unit) {
    TextField(
        value = inputField,
        onValueChange = onValueChange,
        placeholder = { Text("Find for food or restaurant...") },
        leadingIcon = { Icon(Icons.Default.ArrowBack, contentDescription = "Back") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CategoryFilters() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { CategoryChip(text = "Sports", icon = Icons.Default.SportsBasketball) }
        item { CategoryChip(text = "Music", icon = Icons.Default.MusicNote) }
        item { CategoryChip(text = "Food", icon = Icons.Default.Restaurant) }
    }
}

@Composable
fun CategoryChip(text: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White)
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun HobbyLocationDetails(location: HobbyLocation) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(location.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(location.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Implement directions */ }) {
            Text("Get Directions")
        }
    }
}