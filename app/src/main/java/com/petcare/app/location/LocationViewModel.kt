package com.petcare.app.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

private const val TARGET_LAT = 48.2921
private const val TARGET_LNG = 25.9358
private const val TARGET_NAME = "Притулок для тварин, Чернівці"

sealed class LocationUiState {
    data object Idle : LocationUiState()
    data object Loading : LocationUiState()
    data class Success(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Float,
        val updatedAt: String,
        val distanceToTarget: Int
    ) : LocationUiState()

    data class Error(val message: String) : LocationUiState()
}

class LocationViewModel(context: Context) : ViewModel() {

    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    val targetName: String = TARGET_NAME

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        _uiState.value = LocationUiState.Loading

        fusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                updateState(location)
            } else {
                requestSingleUpdate()
            }
        }.addOnFailureListener {
            _uiState.value = LocationUiState.Error("Не вдалося отримати локацію: ${it.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestSingleUpdate() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMaxUpdates(1)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { updateState(it) }
                    ?: run {
                        _uiState.value = LocationUiState.Error("Локацію не вдалося визначити")
                    }
                fusedClient.removeLocationUpdates(this)
                locationCallback = null
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            locationCallback!!,
            Looper.getMainLooper()
        ).addOnFailureListener {
            _uiState.value = LocationUiState.Error("Помилка запиту локації: ${it.message}")
        }
    }

    private fun updateState(location: Location) {
        val time =
            SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale("uk")).format(Date(location.time))
        val distance =
            haversineMeters(location.latitude, location.longitude, TARGET_LAT, TARGET_LNG)
        _uiState.value = LocationUiState.Success(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            updatedAt = time,
            distanceToTarget = distance
        )
    }

    private fun haversineMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Int {
        val r = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (r * c).roundToInt()
    }

    override fun onCleared() {
        super.onCleared()
        locationCallback?.let { fusedClient.removeLocationUpdates(it) }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel: $modelClass")
        }
    }
}