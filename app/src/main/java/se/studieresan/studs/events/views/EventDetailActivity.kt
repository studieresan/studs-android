package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_detail.*
import se.studieresan.studs.R
import se.studieresan.studs.data.Event
import se.studieresan.studs.util.MapUtils

class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, EventDetailActivity::class.java).apply {
            putExtra("Event", event)
        }
    }

    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        event = intent.getParcelableExtra("Event")
        with (event) {
            tv_company_name.text = companyName
            tv_company_location.text = location
            tv_private_description.text = privateDescription
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = MapUtils.getLatLngFromAddress(this, event.location)
        googleMap.addMarker(MarkerOptions().position(position).title(event.location))
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
    }
}
