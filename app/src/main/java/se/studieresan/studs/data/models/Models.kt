package se.studieresan.studs.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

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

    fun getPreEventForm(): String? = beforeSurveys.getOrNull(0)
    fun getPostEventForm(): String? = afterSurveys.getOrNull(0)
}

enum class CompanyFamiliarity {
    YES, SOMEWHAT, NO
}

enum class EventImpact {
    POSITIVE, NEUTRAL, NEGATIVE
}

// God have mercy on my soul
data class EventForm(
    val interestInRegularWork: Int,
    val interestInCompanyMotivation: String,
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
            |interestInRegularWork, viewOfCompany, interestInCompanyMotivation, familiarWithCompany
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
    val interestInRegularWork: Int,
    val viewOfCompany: String,
    val interestInCompanyMotivation: String,
    val familiarWithCompany: CompanyFamiliarity
): Parcelable

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
): Parcelable

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
