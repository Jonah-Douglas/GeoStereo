package com.campfire.geostereo.data

import com.campfire.geostereo.model.ExLocation
import com.google.android.gms.maps.model.LatLng


/**
 *  Object to store example locations if user opts to not give location permissions.
 */
object DataSource {

    val exLocations: List<ExLocation> = listOf(
        ExLocation(
            "Boulevard Park- Bellingham, WA",
            LatLng(48.7317419296335, -122.50242832935756)
        ),
        ExLocation(
            "Whatcom Family YMCA- Bellingham, WA",
            LatLng(48.75072092658313, -122.4702429813274)
        ),
        ExLocation(
            "Bayview Cemetery- Bellingham, WA",
            LatLng(48.751037062459446, -122.43407922456768)
        ),
        ExLocation(
            "Zuanich Point Park- Bellingham, WA",
            LatLng(48.75248937345985, -122.50255823503436)
        ),
        ExLocation(
            "The Royal Night Club- Bellingham, WA",
            LatLng(48.74895595923126, -122.4773025744048)
        ),
        ExLocation(
            "Bellingham Police Department",
            LatLng(48.75810556229714, -122.47457751415574)
        ),
        ExLocation(
            "Moscow, Russia",
            LatLng(56.03355613026854, 37.50014840470158)
        ),
        ExLocation(
            "London, United Kingdom",
            LatLng(51.545642194779965, -0.10709818581544425)
        )
    )
}