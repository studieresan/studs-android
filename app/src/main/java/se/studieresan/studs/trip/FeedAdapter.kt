package se.studieresan.studs.trip

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

    inner class FeedItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val userName: TextView = view.findViewById(R.id.tv_user_name)
        private val message: TextView = view.findViewById(R.id.tv_message)
        private val location: TextView = view.findViewById(R.id.tv_location)
        private val time: TextView = view.findViewById(R.id.tv_time_stamp)
        private val picture: ImageView = view.findViewById(R.id.iv_feed_item)

        fun bind(feedItem: FeedItem) {
            userName.text = feedItem.user
            message.text = feedItem.message
            time.text = feedItem.getTimeAgo()
            location.visibility = if (feedItem.includeLocation) {
                View.VISIBLE
            } else {
                View.GONE
            }
            location.text = feedItem.locationName

            Glide.with(view.context)
                .load(feedItem.picture)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(ColorDrawable(Color.BLACK))
                .into(picture)
        }
    }
}
