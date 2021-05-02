package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HappeningInput(
        val host: String,
        val participants: List<String>,
        var location: Location,
        val title: String?,
        var emoji: String? = "",
        var description: String? = "",
        ) : Parcelable


