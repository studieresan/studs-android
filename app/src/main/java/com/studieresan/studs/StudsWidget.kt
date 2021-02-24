package com.studieresan.studs

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.studieresan.studs.events.adapters.EventAdapter
import com.studieresan.studs.net.StudsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.*
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
class StudsWidget : AppWidgetProvider() {

    @Inject
    lateinit var studsRepository: StudsRepository

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        StudsApplication.applicationComponent.inject(this)
        
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.studs_widget)

        // test/map should be switched out to something else...
        val test = studsRepository
                .getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .forEach { events ->
                    println("fetched events widget...")

                    val today = LocalDateTime.now()
                    val orderedEvents = events.data.events.sortedByDescending { it.getDate() }

                    val nextEvent =
                            orderedEvents.firstOrNull { LocalDateTime.parse(it.date, DATE_FORMATTER) >= today }

                    val parsedDate = LocalDateTime.parse(nextEvent?.date, DATE_FORMATTER)
                    val minutesDisplayFormat = if (parsedDate.minute < 10) "0${parsedDate.minute}" else parsedDate.minute.toString()
                    val time = "${parsedDate.hour}:$minutesDisplayFormat"
                    val displayDate = "${parsedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${parsedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${parsedDate.dayOfMonth.toString()}, $time"

                    views.setTextViewText(R.id.widget_company, nextEvent?.company?.name)
                    views.setTextViewText(R.id.widget_date, displayDate)

                    // switch countdown text to built-in relative time function
                    // views.setTextViewText(R.id.widget_countdown, getRelativeTimeSpanString(R, nextEvent.date, today))
                    views.setTextViewText(R.id.widget_countdown, getCountdown(parsedDate))

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCountdown(date: LocalDateTime): String {
        val difference = LocalDateTime.now().until(date, ChronoUnit.DAYS).toInt()
        return when (difference) {
            0 -> Resources.getSystem().getString(R.string.widget_today)
            1 -> Resources.getSystem().getString(R.string.widget_tomorrow)
            else -> {
                "${Resources.getSystem().getString(R.string.widget_countdown_in)} $difference ${Resources.getSystem().getString(R.string.widget_countdown_days)}"
            }
        }
    }

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}

