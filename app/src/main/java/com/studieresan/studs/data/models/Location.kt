package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
        val type: String,
        val geometry: LocationGeometry,
        val properties: LocationProperties? = null,
) : Parcelable
