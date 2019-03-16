package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.Address
import se.studieresan.studs.data.Event
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.events.contracts.EventDetailContract
import se.studieresan.studs.events.presenters.EventDetailPresenter
import se.studieresan.studs.util.MapUtils

class EventDetailActivity : StudsActivity(), EventDetailContract.View, OnMapReadyCallback {

    private lateinit var event: Event
    private lateinit var presenter: EventDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        event = intent.getParcelableExtra(IntentExtra.EVENT)
        with(event) {
            tv_company_name.text = companyName
            tv_company_location.text = location
            tv_private_description.text = if (privateDescription?.isNotEmpty() == true)
                privateDescription
            else
                getString(R.string.no_description_available)
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val parsedDate = LocalDateTime.parse(event.date, dateFormatter)
            tv_month.text = parsedDate.month.name.substring(0, 3)
            tv_day.text = parsedDate.dayOfMonth.toString()

            val minutesDisplayFormat = if (parsedDate.minute < 10) "0${parsedDate.minute}" else parsedDate.minute.toString()
            val startTime = "${parsedDate.hour}:$minutesDisplayFormat"
            val endTime = "${parsedDate.plusHours(3).hour}:$minutesDisplayFormat"
            tv_event_time.text = getString(R.string.event_time_placeholder, startTime, endTime)
        }

        presenter = EventDetailPresenter(this, event)

        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).run {
            getMapAsync(this@EventDetailActivity)
        }

        tv_company_location.setOnClickListener {
            presenter.didPressCompanyLocation()
        }

        btn_pre_event.run {
            setOnClickListener { presenter.didClickPreEventFormButton() }
            isEnabled = event.getPreEventForm() != null
        }

        btn_post_event.run {
            setOnClickListener { presenter.didClickPostEventFormButton() }
            isEnabled = event.getPostEventForm() != null
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

    override fun openGoogleMapsNavigation(address: Address) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${address.value}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, getString(R.string.google_maps_unavailable), Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, EventDetailActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }

        private const val ZOOM_FACTOR = 13f
    }
}
