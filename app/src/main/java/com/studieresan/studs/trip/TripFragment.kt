package com.studieresan.studs.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.studieresan.studs.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val PERMISSION_FINE_LOCATION = 1

class TripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        configureView(view)
        return view
    }

    private fun getRandomExcitedEmoji(): String {
        val emojis = listOf("🥳", "🤯", "😍", "✨", "🎂", "☀️", "💃", "🍻", "😇", "🤩", "🥰", "🚀", "🇸🇪", "🏞", "🦌", "🏠", "🚞")
        return emojis.random()
    }

    private fun configureView(view: View) {
        val number: TextView = view.findViewById<Button>(R.id.tv_countdown_number)
        val header: TextView = view.findViewById<Button>(R.id.countdown_emojis)
        val today = LocalDate.now()
        var tripDate = LocalDate.of(2021, 6, 14)
        val daysLeft = today.until(tripDate, ChronoUnit.DAYS)

        number.text = daysLeft.toString()
        header.text = getRandomExcitedEmoji().plus(getRandomExcitedEmoji())
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
