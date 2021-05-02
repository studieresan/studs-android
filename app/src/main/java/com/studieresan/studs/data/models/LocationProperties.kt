package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationProperties(
        val name: String,
) : Parcelable
