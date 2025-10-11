package com.example.hobbyhub.mainui.mapscreen.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapState(
    val lastKnownLocation: LatLng? = null,
    val hobbyLocations: List<HobbyLocation> = emptyList(),
    val selectedLocation: HobbyLocation? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val searchQuery: String = ""
)

data class HobbyLocation(
    val id: Int,
    val name: String,
    val location: LatLng,
    val description: String,
    val category: String,
    val icon: ImageVector,
    val color: Color,
    val dateTime: String
)

data class Category(
    val name: String,
    val icon: ImageVector
)

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    private val _mapState = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                viewModelScope.launch {
                    _mapState.value = _mapState.value.copy(
                        lastKnownLocation = LatLng(location.latitude, location.longitude)
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    fun onMarkerClick(location: HobbyLocation) {
        viewModelScope.launch {
            _mapState.value = _mapState.value.copy(selectedLocation = location)
        }
    }

    fun onCategorySelected(category: Category) {
        viewModelScope.launch {
            _mapState.value = _mapState.value.copy(
                selectedCategory = if (_mapState.value.selectedCategory == category) null else category
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _mapState.update { it.copy(searchQuery = query) }
    }

    fun clearSelectedLocation() {
        viewModelScope.launch {
            _mapState.value = _mapState.value.copy(selectedLocation = null)
        }
    }

    init {
        viewModelScope.launch {
            val categories = listOf(
                Category("Sports", Icons.Default.DirectionsRun),
                Category("Music", Icons.Default.MusicNote),
                Category("Art", Icons.Default.Brush),
                Category("Games", Icons.Default.SportsEsports)
            )

            _mapState.value = _mapState.value.copy(
                categories = categories,
                hobbyLocations = listOf(
                    HobbyLocation(1, "Central Park Painting", LatLng(40.785091, -73.968285), "Outdoor painting group.", "Art", Icons.Default.Brush, Color(0xFF4CAF50), "Wed, Oct 15 ・ 5:30 PM"),
                    HobbyLocation(2, "Brooklyn Bridge Photos", LatLng(40.706086, -73.996864), "Meetup for photographers.", "Photography", Icons.Default.CameraAlt, Color(0xFFFFC107), "Thu, Oct 16 ・ 10:00 AM"),
                    HobbyLocation(3, "Union Square Chess", LatLng(40.7359, -73.9911), "Casual chess games.", "Games", Icons.Default.SportsEsports, Color(0xFF9C27B0), "Fri, Oct 17 ・ 1:00 PM"),
                    HobbyLocation(4, "Prospect Park Runners", LatLng(40.6602, -73.9690), "Morning running club.", "Sports", Icons.Default.DirectionsRun, Color(0xFFF44336), "Sat, Oct 18 ・ 8:00 AM"),
                    HobbyLocation(5, "Williamsburg Music Jam", LatLng(40.7144, -73.9565), "Acoustic jam session.", "Music", Icons.Default.MusicNote, Color(0xFF2196F3), "Sun, Oct 19 ・ 3:00 PM")
                )
            )
        }
    }
}