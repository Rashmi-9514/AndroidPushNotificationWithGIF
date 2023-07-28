package com.example.clevertapexampleapplication.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit

class LocationTracker {

    // declare a global variable FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // in onCreate() initialize FusedLocationProviderClient
    //

    private var context: Context

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    constructor(context: Context){
        this.context = context
        setupClient()
    }

    private fun setupClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(3))
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(3))
            .build()
    }


    /**
     * call this method in onCreate
     * onLocationResult call when location is changed
     */
     fun getLocationUpdates(locationTrackingCallback: LocationTrackingCallback)
    {
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if (p0.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        p0.lastLocation
                    location?.let { locationTrackingCallback.receiveLocation(it) }
                    // use your location object
                    // get latitude , longitude and other info from this
                }
            }
        }



    }

    //start location updates
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    interface LocationTrackingCallback{
       fun receiveLocation(location: Location)
    }


}