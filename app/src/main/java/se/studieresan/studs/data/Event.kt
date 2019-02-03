package se.studieresan.studs.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Event(
    val id: String,
    val companyName: String = "",
    @SerializedName("privateDescription")
    val privateDescription: String? = null,
    val publicDescription: String? = null,
    val date: Date,
    val beforeSurveys: List<String> = emptyList(),
    val afterSurveys: List<String> = emptyList(),
    val location: String = "",
    val pictures: List<String> = emptyList(),
    val responsible: String = ""
)
