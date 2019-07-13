package se.studieresan.studs.data.models

import com.google.firebase.database.Exclude
import java.util.concurrent.TimeUnit

data class FeedItem(
    var key: String = "",
    var user: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var message: String = "",
    var timestamp: Long = 0,
    var locationName: String = "",
    var picture: String = "",
    var includeLocation: Boolean = true
) {

    @Exclude
    fun getTimeAgo(): String {
        val minutesAgo = (System.currentTimeMillis() / 1000 - timestamp) / 60

        return when {
            minutesAgo.toInt() == 0 -> "just now"
            minutesAgo.toInt() == 1 -> "a minute ago"
            minutesAgo.toInt() in 2..60 -> "$minutesAgo min ago"
            else -> String.format(
                "%01d h, %02d min ago",
                TimeUnit.MINUTES.toHours(minutesAgo),
                TimeUnit.MINUTES.toMinutes(minutesAgo) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MINUTES.toHours(minutesAgo))
            )
        }
    }
}
