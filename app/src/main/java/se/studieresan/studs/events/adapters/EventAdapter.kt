package se.studieresan.studs.events.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import se.studieresan.studs.R
import se.studieresan.studs.data.Event

class EventAdapter: ListAdapter<Event, EventAdapter.EventViewHolder>(Event.DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int)
            = holder.bind(getItem(position))

    inner class EventViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val companyName: TextView = view.findViewById(R.id.tv_company_name)
        private val companyLocation: TextView = view.findViewById(R.id.tv_company_location)

        fun bind(event: Event) {
            companyName.text = event.companyName
            companyLocation.text = event.location
        }
    }
}