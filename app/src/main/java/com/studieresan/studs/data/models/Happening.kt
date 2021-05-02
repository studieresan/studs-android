package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Happening(
        val id: String,
        val title: String?,
        val host: User? = null,
        val created: String? = "",
        var emoji: String? = "",
        var description: String? = "",
        var location: Location,
        ) : Parcelable


