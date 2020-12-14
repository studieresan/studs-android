package se.studieresan.studs.trip

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.StudsPreferences
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val PERMISSION_FINE_LOCATION = 1

class TripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        configureView(view)
        return view
    }

    private fun getRandomExcitedEmoji(): String {
        val emojis = listOf("🥳", "🤯", "😍", "✨", "🎂", "☀️", "💃", "🍻", "😇", "🤩", "🥰", "🐳", "🇮🇸", "🇩🇪", "🇮🇹")
        return emojis.random()
    }

    private fun configureView(view: View) {
        val number: TextView = view.findViewById<Button>(R.id.countdown_number)
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
