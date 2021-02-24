package com.studieresan.studs

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.view.View
import android.widget.RemoteViews
import com.studieresan.studs.events.adapters.EventAdapter
import com.studieresan.studs.net.StudsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import org.threeten.bp.LocalDate
import timber.log.Timber
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

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.studs_widget)

        // test/map should be switched out to something else...
        val test = studsRepository
                .getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { events ->
                    println("fetched events widget...")

                    val today = LocalDate.now()
                    val orderedEvents = events.data.events.sortedByDescending { it.getDate() }
                    val nextEvent =
                            orderedEvents.firstOrNull { LocalDate.parse(it.date, EventAdapter.DATE_FORMATTER) >= today }

                    val parsedDate = LocalDate.parse(nextEvent?.date, EventAdapter.DATE_FORMATTER)
                    val minutesDisplayFormat = if (parsedDate.minute < 10) "0${parsedDate.minute}" else parsedDate.minute.toString()
                    val time = "${parsedDate.hour}:$minutesDisplayFormat"
                    val displayDate = "${parsedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${parsedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${parsedDate.dayOfMonth.toString()}, ${time}"

                    views.setTextViewText(R.id.widget_company, nextEvent?.company?.name)
                    views.setTextViewText(R.id.widget_date, displayDate)

                    // switch countdown text to built-in relative time function
                    // views.setTextViewText(R.id.widget_countdown, getRelativeTimeSpanString(R, nextEvent.date, today))
                    views.setTextViewText(R.id.widget_countdown, getCountdown(parsedDate))

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
    }

    private fun getCountdown(date: LocalDate): string {
        val difference = LocalDate.now().until(date).DAYS
        return when (difference) {
            0 -> getString(R.string.widget_today)
            1 -> getString(R.string.widget_tomorrow)
            else -> {
                "${getString(R.string.widget_countdown_in)} ${difference} ${getString(R.string.widget_countdown_days)}"
            }
        }
    }
}

