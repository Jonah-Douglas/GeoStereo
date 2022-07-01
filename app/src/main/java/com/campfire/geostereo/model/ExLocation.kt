package com.campfire.geostereo.model

import com.google.android.gms.maps.model.LatLng

/**
 *  Data class to represent example app locations.
 */
data class ExLocation(
    val name: String,
    val latLng: LatLng
)