package se.studieresan.studs.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

