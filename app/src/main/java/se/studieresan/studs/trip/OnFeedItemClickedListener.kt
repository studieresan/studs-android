package se.studieresan.studs.trip

import se.studieresan.studs.data.models.FeedItem

interface OnFeedItemClickedListener {
    fun onFeedItemClicked(feedItem: FeedItem)
}
