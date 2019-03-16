package se.studieresan.studs.data

import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import se.studieresan.studs.data.models.FirebaseRequest

interface FirebaseService {

    @Headers("Content-Type: application/json")
    @POST("send")
    fun sendNotificationMessage(@Header("Authorization") authorization: String, @Body requestBody: FirebaseRequest): Completable
}
