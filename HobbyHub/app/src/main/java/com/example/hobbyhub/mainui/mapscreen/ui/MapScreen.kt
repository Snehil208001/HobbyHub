package com.example.hobbyhub.mainui.mapscreen.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.GpsFixed
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hobbyhub.R
import com.example.hobbyhub.core.utils.composableToBitmapDescriptor
import com.example.hobbyhub.mainui.mapscreen.viewmodel.Category
import com.example.hobbyhub.mainui.mapscreen.viewmodel.HobbyLocation
import com.example.hobbyhub.mainui.mapscreen.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
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
    val parentComposition = rememberCompositionContext()

    val mapStyle = remember { MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = mapStyle,
                isMyLocationEnabled = true
            )
        )
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) viewModel.getDeviceLocation(context)
        }
    )

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- Combined Filtering Logic ---
        val filteredLocations = mapState.hobbyLocations.filter { hobby ->
            val categoryMatch = mapState.selectedCategory == null || hobby.category.equals(mapState.selectedCategory?.name, ignoreCase = true)
            val searchMatch = mapState.searchQuery.isBlank() ||
                    hobby.name.contains(mapState.searchQuery, ignoreCase = true) ||
                    hobby.description.contains(mapState.searchQuery, ignoreCase = true) ||
                    hobby.category.contains(mapState.searchQuery, ignoreCase = true)
            categoryMatch && searchMatch
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapClick = { viewModel.clearSelectedLocation() },
            contentPadding = PaddingValues(bottom = 180.dp)
        ) {
            filteredLocations.forEach { hobby ->
                var bitmapDescriptor by remember { mutableStateOf<com.google.android.gms.maps.model.BitmapDescriptor?>(null) }

                LaunchedEffect(hobby.icon) {
                    bitmapDescriptor = composableToBitmapDescriptor(context, parentComposition) {
                        HobbyMarkerIcon(icon = hobby.icon, color = hobby.color)
                    }
                }

                bitmapDescriptor?.let {
                    Marker(
                        state = MarkerState(position = hobby.location),
                        title = hobby.name,
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
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                .padding(16.dp)
        ) {
            TopSearchBar(
                query = mapState.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged,
                onGpsClick = {
                    mapState.lastKnownLocation?.let { loc ->
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(loc, 15f)
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilterChips(
                categories = mapState.categories,
                selectedCategory = mapState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )
        }

        HobbyList(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                .padding(bottom = 16.dp),
            locations = filteredLocations
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onGpsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle back */ }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = { Text("Find for food or restaurant...", color = Color.Gray) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        IconButton(onClick = onGpsClick) {
            Icon(Icons.Default.GpsFixed, contentDescription = "Current Location", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category.name) },
                leadingIcon = { Icon(category.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                )
            )
        }
    }
}

@Composable
fun HobbyMarkerIcon(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HobbyList(modifier: Modifier = Modifier, locations: List<HobbyLocation>) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(locations) { location ->
            HobbyCard(location)
        }
    }
}

@Composable
fun HobbyCard(location: HobbyLocation) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.dateTime,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(location.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(location.description, color = Color.Gray, fontSize = 12.sp)
            }
            // --- Directions Button ---
            IconButton(
                onClick = {
                    val gmmIntentUri = Uri.parse("google.navigation:q=${location.location.latitude},${location.location.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = "Get Directions",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}