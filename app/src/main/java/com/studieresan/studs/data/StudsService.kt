package com.studieresan.studs.data

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.studieresan.studs.data.models.Events
import com.studieresan.studs.data.models.ForgotPasswordRequest
import com.studieresan.studs.data.models.LoginResponse
import com.studieresan.studs.data.models.LoginUserRequest
import com.studieresan.studs.data.models.UserResponse

private const val userQuery =
        """query {
          user { id }
        }
    """

private const val eventQuery =
        """query {
        events(studsYear:2021) {
            id
            company {
                name
            }
            studsYear
            privateDescription
            publicDescription
            date
            beforeSurvey
            afterSurvey
            location
            pictures
            published
            responsible {
                id
            }
        }
    }"""


interface StudsService {

    @POST("login")
    fun login(@Body loginUserRequest: LoginUserRequest): Observable<LoginResponse>

    @POST("forgot")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Observable<ResponseBody>

    @GET("graphql?query=$userQuery")
    fun getUser(): Observable<UserResponse>

    @GET("graphql?query=$eventQuery")
    fun getEvents(): Observable<Events>
}
