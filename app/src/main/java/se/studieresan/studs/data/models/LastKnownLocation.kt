package se.studieresan.studs.data.models

data class LastKnownLocation(
        var user: String = "",
        var lat: Double = 0.0,
        var lng: Double = 0.0
)
