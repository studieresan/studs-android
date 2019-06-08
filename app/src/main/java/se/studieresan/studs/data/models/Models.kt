package se.studieresan.studs.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Parcelize
data class Event(
    val id: String,
    val companyName: String = "",
    val privateDescription: String? = null,
    val publicDescription: String? = null,
    val date: String?,
    val beforeSurveys: List<String> = emptyList(),
    val afterSurveys: List<String> = emptyList(),
    val location: String = "",
    val pictures: List<String> = emptyList(),
    val responsible: String = "",
    var latLng: LatLng? = null
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
        }
    }

    fun getDate(): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        return format.parse(date!!)
    }
}

data class FeedItem(
    var key: String = "",
    var user: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var message: String = "",
    var timestamp: Long = 0,
    var locationName: String = "",
    var picture: String = "",
    var includeLocation: Boolean = true
) {

    @Exclude
    fun getTimeAgo(): String {
        val minutesAgo = (System.currentTimeMillis() / 1000 - timestamp) / 60

        return when {
            minutesAgo.toInt() == 0 -> "just now"
            minutesAgo.toInt() == 1 -> "a minute ago"
            minutesAgo.toInt() in 2..60 -> "$minutesAgo min ago"
            else -> String.format(
                "%01d h, %02d min ago",
                TimeUnit.MINUTES.toHours(minutesAgo),
                TimeUnit.MINUTES.toMinutes(minutesAgo) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutesAgo))
            )
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean = oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean = oldItem == newItem
        }
    }
}

enum class CompanyFamiliarity {
    YES, SOMEWHAT, NO
}

enum class EventImpact {
    POSITIVE, NEUTRAL, NEGATIVE
}

// God have mercy on my soul
data class EventForm(
    val interestInRegularWorkBefore: Int? = null,
    val interestInCompanyMotivationBefore: String? = null,
    val interestInRegularWork: Int? = null,
    val interestInCompanyMotivation: String? = null,
    val familiarWithCompany: CompanyFamiliarity? = null,
    val viewOfCompany: String? = null,
    val eventImprovements: String? = null,
    val eventFeedback: String? = null,
    val foodRating: Int? = null,
    val activitiesRating: Int? = null,
    val atmosphereRating: Int? = null,
    val qualifiedToWork: Boolean? = null,
    val eventImpact: EventImpact? = null
)

data class UserResponse(val data: UserHolder)
data class UserHolder(val user: User)
data class User(val id: String)
data class Events(val data: AllEvents)
data class EventForms(val data: AllEventForms)
data class AllEvents(val allEvents: List<Event>)
data class AllEventForms(val eventForms: List<EventForm>)
data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
data class FirebaseRequest(val to: String = "/topics/all", val notification: Map<String, String>)

private val createPreEventFormQuery =
    """mutation CreatePreEventForm(${"$"}eventId: String!, ${"$"}fields: PreEventFormInputType!) {
            |createPreEventForm(eventId: ${"$"}eventId, fields: ${"$"}fields) {
            |interestInRegularWorkBefore, viewOfCompany, interestInCompanyMotivationBefore, familiarWithCompany
            |}
            |}""".trimMargin()

private val createPostEventFormQuery =
    """mutation CreatePostEventForm(${"$"}eventId: String!, ${"$"}fields: PostEventFormInputType!) {
            |createPostEventForm(eventId: ${"$"}eventId, fields: ${"$"}fields) {
            |interestInRegularWork, interestInCompanyMotivation, eventImprovements,
            |eventFeedback, foodRating, activitiesRating, atmosphereRating, qualifiedToWork, eventImpact
            |}
            |}""".trimMargin()

@Parcelize
data class CreatePreEventFormFields(
    val interestInRegularWorkBefore: Int,
    val viewOfCompany: String,
    val interestInCompanyMotivationBefore: String,
    val familiarWithCompany: CompanyFamiliarity
) : Parcelable

@Parcelize
data class CreatePostEventFormFields(
    val interestInRegularWork: Int,
    val interestInCompanyMotivation: String,
    val eventImprovements: String,
    val eventFeedback: String,
    val foodRating: Int,
    val activitiesRating: Int,
    val atmosphereRating: Int,
    val qualifiedToWork: Boolean,
    val eventImpact: EventImpact
) : Parcelable

data class CreatePreEventFormBody(
    val eventId: String,
    val fields: CreatePreEventFormFields
)

data class CreatePostEventFormBody(
    val eventId: String,
    val fields: CreatePostEventFormFields
)

data class CreatePreEventFormRequest(
    val variables: CreatePreEventFormBody,
    val query: String = createPreEventFormQuery,
    val operationName: String = "CreatePreEventForm"
)

data class CreatePostEventFormRequest(
    val variables: CreatePostEventFormBody,
    val query: String = createPostEventFormQuery,
    val operationName: String = "CreatePostEventForm"
)

data class LoginResponse(
    val email: String,
    val token: String,
    val name: String,
    val picture: String,
    val position: String,
    val permissions: List<String>
)
