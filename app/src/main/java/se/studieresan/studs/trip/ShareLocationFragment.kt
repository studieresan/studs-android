package se.studieresan.studs.trip

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import se.studieresan.studs.R
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.data.models.FeedItem
import timber.log.Timber

class ShareLocationFragment : DialogFragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_share_location, container, false) ?: return null
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext())

        val messageView = v.findViewById<EditText>(R.id.et_message)
        val checkbox = v.findViewById<CheckBox>(R.id.checkbox_location)

        v.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            val message = messageView.text.toString().trim()

            val db = FirebaseDatabase.getInstance().getReference("locations")

            val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(requireContext(), fineLocation)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.w("Location permission not given, returning")
                return@setOnClickListener
            }

            val userName = StudsPreferences.getName(requireContext())
            if (checkbox.isChecked) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val data = FeedItem(
                        user = userName,
                        lat = it.latitude,
                        lng = it.longitude,
                        message = message,
                        picture = StudsPreferences.getPicture(requireContext()),
                        locationName = getLocationForLatLng(it.latitude, it.longitude),
                        timestamp = System.currentTimeMillis() / 1000
                    )
                    db.push().setValue(data)
                }
            } else {
                val data = FeedItem(
                    user = userName,
                    message = message,
                    picture = StudsPreferences.getPicture(requireContext()),
                    timestamp = System.currentTimeMillis() / 1000,
                    includeLocation = false
                )
                db.push().setValue(data)
            }
            dismiss()
        }

        return v
    }

    private fun getLocationForLatLng(latitude: Double, longitude: Double): String {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses.first().thoroughfare ?: addresses.first().getAddressLine(0)
    }

    companion object {
        val TAG = "${ShareLocationFragment::class.java.simpleName}_tag"
        fun newInstance() = ShareLocationFragment()
    }
}
