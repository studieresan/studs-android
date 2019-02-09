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
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.events.contracts.EventDetailContract
import se.studieresan.studs.events.presenters.EventDetailPresenter
import se.studieresan.studs.util.MapUtils

class EventDetailActivity : StudsActivity(), EventDetailContract.View, OnMapReadyCallback {
    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, EventDetailActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }
        private const val ZOOM_FACTOR = 13f
    }

    private lateinit var event: Event
    private lateinit var presenter: EventDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        event = intent.getParcelableExtra(IntentExtra.EVENT)
        with(event) {
            tv_company_name.text = companyName
            tv_company_location.text = location
            tv_private_description.text = privateDescription
            if (date == null) {
                tv_event_date.visibility = View.GONE
            } else {
                tv_event_date.text = date.substring(0, 10)
            }
        }

        presenter = EventDetailPresenter(this, event)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tv_company_location.setOnClickListener {
            presenter.didPressCompanyLocation()
        }

        btn_pre_event.setOnClickListener {
            presenter.didClickPreEventFormButton()
        }

        btn_post_event.setOnClickListener {
            presenter.didClickPostEventFormButton()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = MapUtils.getLatLngFromAddress(this, event.location)
        googleMap.run {
            addMarker(MarkerOptions().position(position).title(event.location))
            uiSettings.isZoomGesturesEnabled = false
            uiSettings.isScrollGesturesEnabled = false
            uiSettings.isMapToolbarEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_FACTOR))
        }
    }

    override fun openGoogleMapsNavigation(address: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, getString(R.string.google_maps_unavailable), Toast.LENGTH_LONG).show()
        }
    }
}
