package com.studieresan.studs

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.text.format.DateUtils.*
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.studieresan.studs.net.StudsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StudsWidget : AppWidgetProvider() {

    @Inject
    lateinit var studsRepository: StudsRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        StudsApplication.applicationComponent.inject(this)
        
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.studs_widget)
        val subscribe = studsRepository
                .getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ events ->

                    val today = LocalDateTime.now()
                    val orderedEvents = events.data.events.sortedBy { it.getDate() }
                    val nextEvent =
                            orderedEvents.firstOrNull { LocalDateTime.parse(it.date, DATE_FORMATTER) >= today }

                    if (nextEvent !== null) {

                        val parsedDate = LocalDateTime.parse(nextEvent?.date, DATE_FORMATTER)
                        val odt = OffsetDateTime.now()
                        val zoneOffset = odt.offset
                        val dateInMilli = parsedDate.atOffset(zoneOffset).toInstant().toEpochMilli()
                        val todayInMilli = today.atOffset(zoneOffset).toInstant().toEpochMilli()

                        val displayDate = if (nextEvent?.date !== null) formatDateTime(context, dateInMilli, FORMAT_SHOW_TIME or FORMAT_ABBREV_TIME or FORMAT_SHOW_DATE or FORMAT_NUMERIC_DATE or FORMAT_SHOW_WEEKDAY or FORMAT_ABBREV_MONTH) else null
                        val countdown = if (nextEvent?.date !== null) getRelativeTimeSpanString(dateInMilli, todayInMilli, DAY_IN_MILLIS) else null

                        views.setTextViewText(R.id.widget_company, nextEvent?.company?.name)
                        views.setTextViewText(R.id.widget_date, displayDate)
                        views.setTextViewText(R.id.widget_countdown, countdown)

                    } else {
                        views.setTextViewText(R.id.widget_company, "No upcoming events")
                    }

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }, { error ->
                    println(error)
                })
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}

