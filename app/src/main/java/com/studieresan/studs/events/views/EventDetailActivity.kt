package com.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import com.studieresan.studs.R
import com.studieresan.studs.StudsActivity
import com.studieresan.studs.StudsApplication
import com.studieresan.studs.data.IntentExtra
import com.studieresan.studs.data.models.Event
import com.studieresan.studs.events.contracts.EventDetailContract
import com.studieresan.studs.events.presenters.EventDetailPresenter
import com.studieresan.studs.net.StudsRepository
import javax.inject.Inject

class EventDetailActivity : StudsActivity(), EventDetailContract.View {

    @Inject
    lateinit var studsRepository: StudsRepository

    private lateinit var event: Event
    private lateinit var presenter: EventDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)
        setContentView(R.layout.activity_event_detail)

        event = intent.getParcelableExtra(IntentExtra.EVENT)

        tv_company_name.text = event.company?.name
        tv_company_location.text = event.location
        tv_private_description.text = if (event.privateDescription?.isNotEmpty() == true)
            event.privateDescription
        else
            getString(R.string.no_description_available)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val parsedDate = LocalDateTime.parse(event.date, dateFormatter).plusHours(2)
        tv_month.text = parsedDate.month.name.substring(0, 3)
        tv_day.text = parsedDate.dayOfMonth.toString()

        val minutesDisplayFormat = if (parsedDate.minute < 10) "0${parsedDate.minute}" else parsedDate.minute.toString()
        val startTime = "${parsedDate.hour}:$minutesDisplayFormat"
        val endTime = "${parsedDate.plusHours(3).hour}:$minutesDisplayFormat"
        tv_event_time.text = getString(R.string.event_time_placeholder, startTime, endTime)

        presenter = EventDetailPresenter(this, event, studsRepository)
    }

    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, EventDetailActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }

        private const val ZOOM_FACTOR = 13f
        private const val EVENT_FORM_REQUEST_CODE = 1993
    }
}
