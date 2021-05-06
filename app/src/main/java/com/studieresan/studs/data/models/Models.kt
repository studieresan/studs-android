package com.studieresan.studs.data.models

data class ForgotPasswordRequest(val email: String)
data class LoginUserRequest(val email: String, val password: String)
