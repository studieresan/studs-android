package com.studieresan.studs.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
        val id: String,
        val company: Company? = null,
        val privateDescription: String? = null,
        val publicDescription: String? = null,
        val date: String?,
        val location: String? = "",
        val pictures: List<String> = emptyList(),
        var latLng: LatLng? = null,
        var beforeSurvey: String? = null,
        var afterSurvey: String? = null
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
