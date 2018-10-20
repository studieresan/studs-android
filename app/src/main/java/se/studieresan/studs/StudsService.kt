package se.studieresan.studs

import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface StudsService {
    @POST("login") fun login(@Body loginUserRequest: LoginUserRequest): Completable
    @POST("forgot") fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Completable
}

data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
