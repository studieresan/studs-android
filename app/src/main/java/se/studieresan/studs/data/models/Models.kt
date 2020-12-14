package se.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UserResponse(val data: UserHolder)
data class UserHolder(val user: User)
data class User(val id: String)
data class Events(val data: AllEvents)
data class AllEvents(val events: List<Event>)
data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
data class EventRequest(val studsYear: Int)