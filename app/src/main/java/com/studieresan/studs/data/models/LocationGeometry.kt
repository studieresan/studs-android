package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationGeometry(
        val type: String,
        val coordinates: Array<Float>,
) : Parcelable
