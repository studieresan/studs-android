package se.studieresan.studs.events.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDate.now
import org.threeten.bp.format.DateTimeFormatter
import se.studieresan.studs.R
import se.studieresan.studs.data.models.Event

private const val ZOOM_FACTOR = 13f

private const val FUTURE_EVENT_TITLE = 0
private const val PAST_EVENT_TITLE = 1
private const val FUTURE_EVENT = 2
private const val PAST_EVENT = 3

class EventAdapter(
        private val applicationContext: Context,
        private val didSelectEventCallback: (Event) -> Unit
) : ListAdapter<Event, RecyclerView.ViewHolder>(Event.DIFF_CALLBACK) {

    private var pastEvents = emptyList<Event>()
    private var futureEvents = emptyList<Event>()
    private var nextUpcomingEvent: Event? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        fun inflate(@LayoutRes view: Int) = LayoutInflater.from(parent.context).inflate(view, parent, false)

        return when (viewType) {
            FUTURE_EVENT_TITLE, PAST_EVENT_TITLE -> EventTitleViewHolder(inflate(R.layout.list_item_event_title), viewType)
            FUTURE_EVENT -> EventViewHolder(inflate(R.layout.list_item_event_card))
            PAST_EVENT -> PastEventViewHolder(inflate(R.layout.list_item_past_event_card))
            else -> throw IllegalStateException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PastEventViewHolder -> holder.bind(pastEvents[position - 2 - futureEvents.size])
            is EventViewHolder -> holder.bind(futureEvents[position - 1])
            is EventTitleViewHolder -> holder.bind()
        }
    }

    override fun submitList(list: List<Event>?) {
        list?.let {
            partitionEvents(it)
        }
        super.submitList(list)
    }

    private fun partitionEvents(events: List<Event>) {
        val today: LocalDate = now()
        val (past, future) = events.partition { LocalDate.parse(it.date, DATE_FORMATTER) < today }
        pastEvents = past
        futureEvents = future
        nextUpcomingEvent = futureEvents.firstOrNull()
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> FUTURE_EVENT_TITLE
        in 1..futureEvents.size -> FUTURE_EVENT
        futureEvents.size + 1 -> PAST_EVENT_TITLE
        else -> PAST_EVENT
    }

    inner class EventViewHolder(private val view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
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

    inner class PastEventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val companyName: TextView = view.findViewById(R.id.tv_company_name)
        private val month: TextView = view.findViewById(R.id.tv_month)
        private val day: TextView = view.findViewById(R.id.tv_day)

        fun bind(event: Event) {
            view.setOnClickListener { didSelectEventCallback.invoke(event) }
            companyName.text = event.companyName
            month.text = LocalDate.parse(event.date, DATE_FORMATTER).month.name.substring(0, 3)
            day.text = LocalDate.parse(event.date, DATE_FORMATTER).dayOfMonth.toString()
        }
    }

    inner class EventTitleViewHolder(private val view: View, private val viewType: Int) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_header)

        fun bind() {
            title.text = view.context.getString(if (viewType == FUTURE_EVENT_TITLE) {
                R.string.upcoming_events
            } else {
                R.string.past_events
            })
        }
    }

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
