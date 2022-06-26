package com.campfire.geostereo.ui

import android.Manifest
import android.util.Log
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.SettingsSlicesContract.KEY_LOCATION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.campfire.geostereo.MainActivity
import com.campfire.geostereo.R
import com.campfire.geostereo.databinding.FragmentFindNearestLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FindNearestLocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentFindNearestLocationBinding? = null
    private lateinit var mMap: MapView
    private lateinit var mGoogleMap: GoogleMap
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private lateinit var currentLatLng: LatLng
    private var lastKnownLocation: Location? = null
    private var cameraPosition: CameraPosition? = null

    // Property only valid between onCreateView and onDestroyView, so !! ok
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)

        lastKnownLocation = savedInstanceState?.getParcelable(KEY_LOCATION)
        cameraPosition = savedInstanceState?.getParcelable(KEY_CAMERA_POSITION)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mGoogleMap.let {
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
            outState.putParcelable(KEY_CAMERA_POSITION, cameraPosition)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindNearestLocationBinding.inflate(inflater, container, false)

        mMap = binding.mapView
        mMap.onCreate(savedInstanceState)
        mMap.getMapAsync(this)

        return binding.root
    }

    override fun onPause() {
        mMap.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMap.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findNearestLocation.setOnClickListener {
            // TODO: Google Maps search here (create coroutine call to do searches so it's nonblocking
            // TODO: On Result, navigate to new fragment
            // shows nearest location
            // if nothing nearby, query a weather API
            // TODO: Use final result to query Spotify API (again coroutine call)
            // show song playing in app, have some features such as pause song, skip, etc.
            // if I can't implement spotify in my app, use implicit/explicit Intent to pull up Spotify playlist on their phone and begin playing it
            Toast.makeText(
                context,
                "Implement Google Maps search here, navigate to new fragment showing nearest location and song playing.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        getLocationPermission()

        updateLocationUI()

        getDeviceLocation()
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        currentLatLng =
                            LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                        if (lastKnownLocation != null) {
                            mGoogleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    DEFAULT_ZOOM.toFloat()
                                )
                            )
                            mGoogleMap.addMarker(
                                MarkerOptions()
                                    .title("Your Location")
                                    .position(currentLatLng)
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mGoogleMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            } else {
                mGoogleMap.moveCamera(
                    CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                )
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .title("Your Location")
                        .position(defaultLocation)
                )
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Updates the map's UI settings if user gave access.
     */
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mGoogleMap.isMyLocationEnabled = true
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mGoogleMap.isMyLocationEnabled = false
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /*/**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
            return
        }

        // 2. Check if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }*/

    companion object {
        private const val TAG = "FindNearestLocationFragment::class.java"
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for activity state
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}