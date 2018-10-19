package models

data class StudsEvent(
        val id: String,
        val companyName: String = "",
        val privateDescription: String? = null,
        val publicDescription: String? = null,
        val date: String? = null,
        val beforeSurveys: List<String> = emptyList(),
        val afterSurveys: List<String> = emptyList(),
        val location: String? = null,
        val pictures: List<String> = emptyList()
)