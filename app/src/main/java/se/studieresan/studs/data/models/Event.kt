package se.studieresan.studs.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Event(
    val id: String,
    val companyName: String = "",
    val privateDescription: String? = null,
    val publicDescription: String? = null,
    val date: String?,
    val beforeSurveys: List<String> = emptyList(),
    val afterSurveys: List<String> = emptyList(),
    val location: String = "",
    val pictures: List<String> = emptyList(),
    val responsible: String = "",
    var latLng: LatLng? = null
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
        }
    }

    fun getDate(): Date {
        val format =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        return format.parse(date!!)
    }
}
