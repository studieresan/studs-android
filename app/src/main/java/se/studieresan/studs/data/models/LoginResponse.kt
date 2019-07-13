package se.studieresan.studs.data.models

data class LoginResponse(
    val email: String,
    val token: String,
    val name: String,
    val picture: String,
    val position: String,
    val permissions: List<String>
)
