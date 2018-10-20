package se.studieresan.studs

import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface StudsService {
    @POST("login") fun login(@Body user: User): Completable
}

data class User(val email: String, val password: String)