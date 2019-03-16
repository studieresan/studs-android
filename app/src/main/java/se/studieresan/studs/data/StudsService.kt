package se.studieresan.studs.data

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import se.studieresan.studs.data.models.Events
import se.studieresan.studs.data.models.ForgotPasswordRequest
import se.studieresan.studs.data.models.LoginUserRequest

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

    @GET("graphql?query=$eventQuery")
    fun getEvents(): Observable<Events>
}

