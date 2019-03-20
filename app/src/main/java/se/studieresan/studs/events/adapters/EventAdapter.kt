package se.studieresan.studs.events.adapters

import android.content.Context
import android.graphics.Typeface
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
import se.studieresan.studs.util.exhaustive

private const val ZOOM_FACTOR = 13f

private const val FUTURE_EVENT_TITLE = 0
private const val PAST_EVENT_TITLE = 1
private const val FUTURE_EVENT = 2
private const val PAST_EVENT = 3
private const val NEXT_EVENT_TITLE = 4
private const val NEXT_EVENT = 5
private const val EMPTY = 6

class EventAdapter(
        private val applicationContext: Context,
        private val didSelectEventCallback: (Event) -> Unit
) : ListAdapter<Event, RecyclerView.ViewHolder>(Event.DIFF_CALLBACK) {

    private var pastEvents = emptyList<Event>()
    private var futureEvents = emptyList<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        fun inflate(@LayoutRes view: Int) = LayoutInflater.from(parent.context).inflate(view, parent, false)

        return when (viewType) {
            FUTURE_EVENT_TITLE, PAST_EVENT_TITLE, NEXT_EVENT_TITLE -> EventTitleViewHolder(inflate(R.layout.list_item_event_title), viewType)
            NEXT_EVENT -> NextEventViewHolder(inflate(R.layout.list_item_next_event_card))
            PAST_EVENT, FUTURE_EVENT -> EventViewHolder(inflate(R.layout.list_item_event_card))
            EMPTY -> EmptyViewHolder(inflate(R.layout.list_item_empty))
            else -> throw IllegalStateException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        when (holder) {
            is EventViewHolder -> if (type == PAST_EVENT) {
                if (futureEvents.isNotEmpty()) {
                    holder.bind(pastEvents[position - 4 - futureEvents.size])
                } else {
                    holder.bind(getItem(position - 4))
                }
            } else {
                holder.bind(futureEvents[position - 3])
            }
            is NextEventViewHolder -> holder.bind(futureEvents.first())
            is EventTitleViewHolder -> holder.bind()
            else -> Unit /* Do nothing */
        }.exhaustive
    }

    override fun submitList(list: List<Event>?) {
        list?.let {
            partitionEvents(it)
        }
        super.submitList(list)
    }

    override fun getItemCount(): Int {
        val size = super.getItemCount()
        return if (size == 0) {
            0
        } else {
            size + 4
        }
    }

    private fun partitionEvents(events: List<Event>) {
        val today: LocalDate = now()
        val (past, future) = events.partition { LocalDate.parse(it.date, DATE_FORMATTER) < today }
        pastEvents = past
        futureEvents = future.reversed()
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> NEXT_EVENT_TITLE
        1 -> if (futureEvents.isNotEmpty()) {
            NEXT_EVENT
        } else {
            EMPTY
        }
        2 -> FUTURE_EVENT_TITLE
        in 2..futureEvents.size + 2 -> if (futureEvents.isNotEmpty()) {
            FUTURE_EVENT
        } else {
            EMPTY
        }
        futureEvents.size + 3 -> PAST_EVENT_TITLE
        else -> if (pastEvents.isNotEmpty()) {
            PAST_EVENT
        } else {
            EMPTY
        }
    }

    inner class NextEventViewHolder(private val view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        private var mapView: MapView = view.findViewById(R.id.event_map_view)
        private val companyName: TextView = view.findViewById(R.id.tv_company_name)
        private val month: TextView = view.findViewById(R.id.tv_month)
        private val day: TextView = view.findViewById(R.id.tv_day)

        var map: GoogleMap? = null
            private set

        init {
            mapView.run {
                onCreate(null)
                getMapAsync(this@NextEventViewHolder)
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

    inner class EventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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
            title.text = view.context.getString(when (viewType) {
                FUTURE_EVENT_TITLE -> R.string.upcoming_events
                PAST_EVENT_TITLE -> R.string.past_events
                else -> R.string.next_event
            })
            title.setTypeface(title.typeface, if (viewType == NEXT_EVENT_TITLE) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
