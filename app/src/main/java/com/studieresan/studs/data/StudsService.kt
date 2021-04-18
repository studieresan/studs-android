package com.studieresan.studs.data

import com.studieresan.studs.data.models.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

private const val happeningsQuery =
        """query {
        happenings {
            id
            title
            emoji
            created
            description
            location {
                type
                geometry {
                    type
                    coordinates
                }
                properties {
                    name
                  }
                }
            host {
                id
                firstName
                lastName
            }
            participants {
                id
                firstName
                lastName
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

    @GET("graphql?query=$happeningsQuery")
    fun getHappenings(): Observable<Happenings>
}
