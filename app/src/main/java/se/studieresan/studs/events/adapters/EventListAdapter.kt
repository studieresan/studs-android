package se.studieresan.studs.events.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.studieresan.studs.R
import se.studieresan.studs.data.Event
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(private val events: ArrayList<Event>) : RecyclerView.Adapter<EventListAdapter.EventsViewHolder>() {

    class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var month: TextView = view.findViewById(R.id.event_date_month)
        var day: TextView = view.findViewById(R.id.event_date_day)
        var organizer: TextView = view.findViewById(R.id.event_organizer)
        var address: TextView = view.findViewById(R.id.event_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_event, parent, false)
        return EventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.month.text = SimpleDateFormat("MMM").format(events[position].date).capitalize()
        holder.day.text = events[position].date.day.toString()
        holder.organizer.text = events[position].companyName
        holder.address.text = events[position].location
    }

    override fun getItemCount() = events.size
}