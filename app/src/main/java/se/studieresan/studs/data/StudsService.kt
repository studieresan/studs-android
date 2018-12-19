package se.studieresan.studs.data

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
  fun login(@Body loginUserRequest: LoginUserRequest): Completable

  @POST("forgot")
  fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Completable

  @GET("graphql?query=$eventQuery")
  fun getEvents(): Single<Events>
}

data class Events(val data: AllEvents)
data class AllEvents(val allEvents: List<Event>)
data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
