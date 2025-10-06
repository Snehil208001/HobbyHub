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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapState(
    val lastKnownLocation: LatLng? = null,
    val hobbyLocations: List<HobbyLocation> = emptyList(),
    val selectedLocation: HobbyLocation? = null
)

data class HobbyLocation(
    val id: Int,
    val name: String,
    val location: LatLng,
    val description: String,
    val category: String,
    val icon: ImageVector // Added icon property
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

    fun clearSelectedLocation() {
        viewModelScope.launch {
            _mapState.value = _mapState.value.copy(selectedLocation = null)
        }
    }

    init {
        viewModelScope.launch {
            _mapState.value = _mapState.value.copy(
                hobbyLocations = listOf(
                    HobbyLocation(1, "Central Park Painting", LatLng(40.785091, -73.968285), "Outdoor painting and sketching group.", "Art", Icons.Default.Brush),
                    HobbyLocation(2, "Brooklyn Bridge Photography", LatLng(40.706086, -73.996864), "Meetup for aspiring photographers.", "Photography", Icons.Default.CameraAlt),
                    HobbyLocation(3, "Union Square Chess Club", LatLng(40.7359, -73.9911), "Casual chess games for all levels.", "Games", Icons.Default.SportsEsports),
                    HobbyLocation(4, "Prospect Park Runners", LatLng(40.6602, -73.9690), "Morning running club.", "Sports", Icons.Default.DirectionsRun),
                    HobbyLocation(5, "Williamsburg Music Jam", LatLng(40.7144, -73.9565), "Acoustic jam session.", "Music", Icons.Default.MusicNote)
                )
            )
        }
    }
}