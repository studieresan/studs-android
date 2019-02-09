package se.studieresan.studs.util

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

object MapUtils {

    fun getLatLngFromAddress(context: Context, streetAddress: String): LatLng {
        val coder = Geocoder(context)
        val address = coder.getFromLocationName(streetAddress, 3)
        if (address.isNullOrEmpty()) {
            throw Exception("Could not locate address")
        }
        val location = address[0]
        return LatLng(location.latitude, location.longitude)
    }
}
