package se.studieresan.studs.data

import androidx.recyclerview.widget.DiffUtil

data class Event(
    val id: String,
    val companyName: String = "",
    val privateDescription: String? = null,
    val publicDescription: String? = null,
    val date: String? = null,
    val beforeSurveys: List<String> = emptyList(),
    val afterSurveys: List<String> = emptyList(),
    val location: String = "",
    val pictures: List<String> = emptyList(),
    val responsible: String = ""
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
        }
    }
}
