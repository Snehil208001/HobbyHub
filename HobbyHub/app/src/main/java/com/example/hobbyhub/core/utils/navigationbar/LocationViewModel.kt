package com.example.hobbyhub.core.utils.navigationbar

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {

    private val _location = MutableStateFlow("New York, USA")
    val location: StateFlow<String> = _location

    @SuppressLint("MissingPermission")
    fun fetchLocation(context: Context) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { locationResult ->
            if (locationResult != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(locationResult.latitude, locationResult.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0]
                        _location.value = "${address.locality}, ${address.adminArea}"
                    }
                } catch (e: Exception) {
                    _location.value = "Location not found"
                }
            }
        }
    }
}