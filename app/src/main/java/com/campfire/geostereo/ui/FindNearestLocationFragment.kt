package com.campfire.geostereo.ui

import android.Manifest
import android.util.Log
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations.map
import com.campfire.geostereo.MainActivity.Companion.hasGPS
import com.campfire.geostereo.data.DataSource.exLocations
import com.campfire.geostereo.databinding.FragmentFindNearestLocationBinding
import com.campfire.geostereo.model.ExLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.Flow


/**
 *  Fragment that handles finding a nearby location or example location.
 */
class FindNearestLocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentFindNearestLocationBinding? = null
    private val binding get() = _binding!!

    // layout vars
    private lateinit var mMap: MapView
    private var locationPermissionGranted = false

    // Google Map vars
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var randomLocation: ExLocation
    private lateinit var currentLatLng: LatLng
    private var lastKnownLocation: Location? = null
    private var cameraPosition: CameraPosition? = null


    // Pull a random location from my DataSource
    init {
        randomLocation = exLocations.shuffled()[0]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
            // TODO: Call onAudioFocusChange to handle pausing audio when app is interrupted
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
            // TODO: need to check if user has GPS capability, use Pref DataStore
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
                                .newLatLngZoom(randomLocation.latLng, DEFAULT_ZOOM.toFloat())
                        )
                        mGoogleMap.addMarker(
                            MarkerOptions()
                                .title(randomLocation.name)
                                .position(currentLatLng)
                        )
                        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            } else {
                selectRandomExLocation()
                mGoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        randomLocation.latLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
                updateLocationUI()

                mGoogleMap.addMarker(
                    MarkerOptions()
                        .title(randomLocation.name)
                        .position(randomLocation.latLng)
                )
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     *   Randomly selects a location from ExLocations to use for
     */
    private fun selectRandomExLocation() {
        randomLocation = exLocations.shuffled()[0]
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
            updateLocationUI()
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            // TODO: What do I do here to make the first app use navigate correctly
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