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
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import se.studieresan.studs.R
import se.studieresan.studs.data.models.Event

private const val ZOOM_FACTOR = 13f

class EventAdapter(
        private val applicationContext: Context,
        private val didSelectEventCallback: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(Event.DIFF_CALLBACK) {
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
        private val month: TextView = view.findViewById(R.id.tv_month)
        private val day: TextView = view.findViewById(R.id.tv_day)

        var map: GoogleMap? = null
            private set

        init {
            mapView.run {
                onCreate(null)
                getMapAsync(this@EventViewHolder)
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(applicationContext)
            map = googleMap
            setMapLocation()
        }

        fun bind(event: Event) {
            view.tag = this
            view.setOnClickListener { didSelectEventCallback.invoke(event) }
            mapView.tag = event
            setMapLocation()
            companyName.text = event.companyName
            month.text = LocalDate.parse(event.date, DATE_FORMATTER).month.name.substring(0, 3)
            day.text = LocalDate.parse(event.date, DATE_FORMATTER).dayOfMonth.toString()
        }

        private fun setMapLocation() {
            val data = mapView.tag as Event
            map?.run {
                addMarker(MarkerOptions().position(data.latLng!!).title(data.location))
                moveCamera(CameraUpdateFactory.newLatLngZoom(data.latLng!!, ZOOM_FACTOR))
            }
        }
    }

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
