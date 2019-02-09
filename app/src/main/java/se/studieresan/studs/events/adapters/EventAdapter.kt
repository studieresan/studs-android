package se.studieresan.studs.events.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import se.studieresan.studs.R
import se.studieresan.studs.data.Event
import se.studieresan.studs.util.MapUtils

private const val ZOOM_FACTOR = 13f

class EventAdapter(private val applicationContext: Context): ListAdapter<Event, EventAdapter.EventViewHolder>(Event.DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int)
            = holder.bind(getItem(position))

    inner class EventViewHolder(private val view: View): RecyclerView.ViewHolder(view), OnMapReadyCallback {
        private var mapView: MapView = view.findViewById(R.id.event_map_view)
        private val companyName: TextView = view.findViewById(R.id.tv_company_name)
        private val companyLocation: TextView = view.findViewById(R.id.tv_company_location)

        var map: GoogleMap? = null
            private set

        init {
            mapView.onCreate(null)
            mapView.getMapAsync(this)
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(applicationContext)
            map = googleMap
            setMapLocation()
        }

        fun bind(event: Event) {
            view.tag = this
            mapView.tag = event
            setMapLocation()
            companyName.text = event.companyName
            companyLocation.text = event.location
        }

        private fun setMapLocation() {
            val data = mapView.tag as Event
            val position = MapUtils.getLatLngFromAddress(applicationContext, data.location)
            map?.addMarker(MarkerOptions().position(position).title(data.location))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_FACTOR))
        }
    }
}
