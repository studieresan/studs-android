package com.studieresan.studs.trip

import HappeningCreateMutation
import HappeningsQuery
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.studieresan.studs.R
import com.studieresan.studs.graphql.apolloClient
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import type.GeoJSONFeatureInputType
import type.GeometryInputType
import type.HappeningInput
import type.PropertiesInputType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val PERMISSION_FINE_LOCATION = 1

class TripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        configureView(view)

        val button: Button = view.findViewById<Button>(R.id.btn_create_happening)

        button.setOnClickListener {
            val happening = HappeningInput(
                    host = Input.fromNullable("5fb278e69fca82b6f5faf80f"),
                    participants = Input.fromNullable(listOf("5fb278e69fca82b6f5faf80e", "5fb278e69fca82b6f5faf806")),
                    location = Input.fromNullable(GeoJSONFeatureInputType(type = Input.fromNullable("Feature"),
                            geometry = Input.fromNullable(GeometryInputType(
                                    type = Input.fromNullable(("Point")),
                                    coordinates = Input.fromNullable(listOf(18.030078, 59.344294)))),
                            properties = Input.fromNullable(PropertiesInputType( Input.fromNullable("Idun"))))),
                    title = Input.fromNullable("Coding & crying"),
                    emoji = Input.fromNullable("🥂"),
                    description = Input.fromNullable("Hihihi hänger med Kotten o har det NAJS. Funkar detta äre dags för bubbel ändå"))

        CoroutineScope(Dispatchers.Main).launch {
            val response = try {
                apolloClient(requireContext()).mutate(HappeningCreateMutation(Input.fromNullable(happening))).await()
            } catch (e: ApolloException) {
                null
            }

            val response2 = try {
                apolloClient(requireContext()).query(HappeningsQuery()).await()
            } catch (e: ApolloException) {
                null
            }

            val happenings = response2?.data?.happenings
            println("HAPPENINGS: ")
            println(happenings)

        }
    }

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
