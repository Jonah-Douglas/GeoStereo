package com.campfire.geostereo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.campfire.geostereo.databinding.FragmentFindNearestLocationBinding
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

    // Property only valid between onCreateView and onDestroyView, so !! ok
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
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
        // enableMyLocation()
        // serious git issues
        /*val mFusedLocationClient =
        var currentLocation =
        var caPosition = CameraPosition.builder()
            .target()
            .zoom(18.0F)
            .tilt(45.0F)
            .build()*/

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mGoogleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    /*@SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }*/
}