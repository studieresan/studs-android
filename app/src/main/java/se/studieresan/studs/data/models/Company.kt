package se.studieresan.studs.data.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    val id: String?,
    val name: String
) : Parcelable
