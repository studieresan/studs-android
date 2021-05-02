package com.studieresan.studs.data.models

data class UserResponse(val data: UserHolder)
data class UserHolder(val user: User)
data class Events(val data: AllEvents)
data class AllEvents(val events: List<Event>)
data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
data class Happenings(val data: AllHappenings)
data class AllHappenings(val happenings: List<Happening>)

data class CreateHappeningRequest(
    val variables: HappeningInput,
    val query: String = "happeningCreate",
    val operationName: String = "happeningCreate",
)
