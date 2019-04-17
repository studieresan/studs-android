package se.studieresan.studs.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckInAdapter(private var items: Array<String>) : RecyclerView.Adapter<CheckInAdapter.CheckInViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInViewHolder {
        val textView = LayoutInflater
            .from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return CheckInViewHolder(textView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CheckInViewHolder, position: Int) = holder.bind(items[position])

    fun setItems(newItems: Array<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CheckInViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(name: String) {
            textView.text = name
        }
    }
}
