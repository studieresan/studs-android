package com.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: String?,
        val firstName: String?,
        val lastName: String?,
        val info: UserInfo,
) : Parcelable
