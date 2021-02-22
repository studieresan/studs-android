package com.studieresan.studs

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
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

        println("updating widget...")
        // fetch events
        // studsrepository is not initilizated when widget is created... should the widget have it's own query definition/service?
        studsRepository
                .getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { events ->
                    println("fetched events widget...")

                    val today = LocalDate.now()
                    val orderedEvents = events.data.events.sortedByDescending { it.getDate() }
                    val nextEvent =
                            orderedEvents.firstOrNull { LocalDate.parse(it.date, EventAdapter.DATE_FORMATTER) >= today }
                    views.setTextViewText(R.id.widget_company, nextEvent?.company?.name)
                    views.setTextViewText(R.id.widget_date, nextEvent?.date)

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
    }
}

/*
internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.studs_widget)
    views.setTextViewText(R.id.widget_company, widgetText)

    // fetch events
    studsRepository
            .getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { events ->
                val today = LocalDate.now()
                val orderedEvents = events.data.events.sortedByDescending { it.getDate() }
                val nextEvent =
                        orderedEvents.firstOrNull { LocalDate.parse(it.date, EventAdapter.DATE_FORMATTER) >= today }
                orderedEvents
            }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
*/
