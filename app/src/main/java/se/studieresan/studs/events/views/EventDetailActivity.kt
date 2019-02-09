package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_detail.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.Event
import se.studieresan.studs.util.MapUtils

class EventDetailActivity : StudsActivity(), OnMapReadyCallback {
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
            if (date == null) {
                tv_event_date.visibility = View.GONE
            } else {
                tv_event_date.text = date.substring(0, 10)
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        tv_company_location.setOnClickListener { navigateToAddressForEvent(event) }
        btn_pre_event.setOnClickListener {
            event.getPreEventForm()?.let {form ->
                openBrowser(form)
            }
        }
        btn_post_event.setOnClickListener {
            event.getPostEventForm()?.let {form ->
                openBrowser(form)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = MapUtils.getLatLngFromAddress(this, event.location)
        googleMap.addMarker(MarkerOptions().position(position).title(event.location))
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
    }

    private fun navigateToAddressForEvent(event: Event) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${event.location}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "You need to have Google Maps installed.", Toast.LENGTH_LONG).show()
        }
    }
}
