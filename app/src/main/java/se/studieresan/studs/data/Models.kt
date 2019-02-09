package se.studieresan.studs.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
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
        val responsible: String = ""
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
        }
    }

    fun getDate(): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        return format.parse(date!!)
    }

    fun getPreEventForm(): String? = beforeSurveys.getOrNull(0)
    fun getPostEventForm(): String? = afterSurveys.getOrNull(0)
}

inline class Email(val value: String)
inline class Password(val value: String)
