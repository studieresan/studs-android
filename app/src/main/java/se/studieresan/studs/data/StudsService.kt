package se.studieresan.studs.data

import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import se.studieresan.studs.data.models.CreatePostEventFormRequest
import se.studieresan.studs.data.models.CreatePreEventFormRequest
import se.studieresan.studs.data.models.EventForms
import se.studieresan.studs.data.models.Events
import se.studieresan.studs.data.models.ForgotPasswordRequest
import se.studieresan.studs.data.models.LoginUserRequest
import se.studieresan.studs.data.models.UserResponse

private const val userQuery =
    """query {
          user { id }
        }
    """

private const val eventQuery =
    """query {
        allEvents {
            id
            companyName
            schedule
            privateDescription
            publicDescription
            date
            beforeSurveys
            afterSurveys
            location
            pictures
            published
            responsible
        }
    }"""

interface StudsService {
    @POST("login")
    fun login(@Body loginUserRequest: LoginUserRequest): Observable<ResponseBody>

    @POST("forgot")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Observable<ResponseBody>

    @GET("graphql?query=$userQuery")
    fun getUser(): Observable<UserResponse>

    @GET("graphql?query=$eventQuery")
    fun getEvents(): Observable<Events>

    @POST("graphql?")
    fun postPreEventForm(@Body preEventFormRequest: CreatePreEventFormRequest): Completable

    @POST("graphql?")
    fun postPostEventForm(@Body postEventFormRequest: CreatePostEventFormRequest): Completable

    @GET("graphql")
    fun getFormsForEvent(@Query("query") query: String): Observable<EventForms>
}


