package se.studieresan.studs.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val id: String,
    val companyName: String = "",
    val privateDescription: String? = null,
    val publicDescription: String? = null,
    val date: String? = null,
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
}

inline class Email(val value: String)
inline class Password(val value: String)
