package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
        val picture: String?,
) : Parcelable
