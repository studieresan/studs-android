package se.studieresan.studs.events.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDate.now
import se.studieresan.studs.R
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.events.adapters.EventAdapter
import se.studieresan.studs.net.StudsRepository
import se.studieresan.studs.util.MapUtils
import timber.log.Timber
import javax.inject.Inject

class EventsFragment : Fragment() {

    private lateinit var adapter: EventAdapter
    private var disposable: Disposable? = null

    @Inject
    lateinit var studsRepository: StudsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)
        adapter = EventAdapter(requireActivity().applicationContext) { event -> displayEventDetails(event) }
        fetchEvents(false)
        swipe_refresh.setOnRefreshListener { fetchEvents(true) }
        rv_events.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@EventsFragment.adapter
            setHasFixedSize(true)
            setRecyclerListener(recycleListener)
        }
    }

    private fun fetchEvents(refresh: Boolean) {
        disposable?.dispose()
        disposable = studsRepository
            .getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { events ->
                val today = now()
                val orderedEvents = events.data.allEvents.sortedByDescending { it.getDate() }
                val nextEvent =
                    orderedEvents.firstOrNull { LocalDate.parse(it.date, EventAdapter.DATE_FORMATTER) > today }
                nextEvent?.let {
                    it.latLng = MapUtils.getLatLngFromAddress(requireContext(), it.location)
                }
                orderedEvents
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { if (!refresh) progress_bar.visibility = View.VISIBLE }
            .doOnTerminate { if (!refresh) progress_bar.visibility = View.GONE }
            .subscribe({
                swipe_refresh.isRefreshing = false
                adapter.submitList(it)
            }, { t ->
                swipe_refresh.isRefreshing = false
                Timber.e(t)
            })
    }

    private fun displayEventDetails(event: Event) =
        startActivity(EventDetailActivity.makeIntent(requireContext(), event))

    private val recycleListener = RecyclerView.RecyclerListener { holder ->
        val eventHolder = holder as? EventAdapter.NextEventViewHolder
        eventHolder?.map?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable = null
    }
}
