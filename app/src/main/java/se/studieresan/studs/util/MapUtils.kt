package se.studieresan.studs.util

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

object MapUtils {

    private const val MAX_RESULTS = 3

    fun getLatLngFromAddress(context: Context, streetAddress: String): LatLng {
        val coder = Geocoder(context)
        val address = coder.getFromLocationName(streetAddress, MAX_RESULTS)
        if (address.isNullOrEmpty()) {
            throw Exception("Could not locate address")
        }
        val location = address.first()
        return LatLng(location.latitude, location.longitude)
    }
}
