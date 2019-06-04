package se.studieresan.studs.trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import se.studieresan.studs.R
import se.studieresan.studs.data.models.FeedItem

class FeedAdapter(
    private val listener: OnFeedItemClickedListener
) : ListAdapter<FeedItem, FeedAdapter.FeedItemViewHolder>(FeedItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        fun inflate(@LayoutRes view: Int) = LayoutInflater.from(parent.context).inflate(view, parent, false)
        return FeedItemViewHolder(inflate(R.layout.list_item_feed))
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        val feedItem = getItem(position)
        holder.itemView.setOnClickListener {
            listener.onFeedItemClicked(feedItem)
        }
        holder.bind(feedItem)
    }

    inner class FeedItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userName: TextView = view.findViewById(R.id.tv_user_name)
        private val message: TextView = view.findViewById(R.id.tv_message)
        private val location: TextView = view.findViewById(R.id.tv_location)

        fun bind(feedItem: FeedItem) {
            userName.text = feedItem.user
            message.text = feedItem.message
            if (feedItem.location.isEmpty()) {
                location.visibility = View.GONE
            } else {
                location.text = feedItem.location
            }
        }
    }
}
