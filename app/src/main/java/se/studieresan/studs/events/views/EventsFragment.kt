package se.studieresan.studs.events.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.data.Event
import se.studieresan.studs.events.adapters.EventAdapter
import se.studieresan.studs.net.StudsRepository
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
        fetchEvents()
        swipe_refresh.setOnRefreshListener { fetchEvents() }
        rv_events.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@EventsFragment.adapter
            setHasFixedSize(true)
            setRecyclerListener(recycleListener)
        }
    }

    private fun fetchEvents() {
        disposable?.dispose()
        disposable = studsRepository
                .getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progressBar?.visibility = View.VISIBLE
                }
                .doFinally {
                    swipe_refresh?.isRefreshing = false
                    progressBar?.visibility = View.GONE
                }
                .subscribe({ adapter.submitList(it.data.allEvents) }, { t -> Timber.d(t) })
    }

    private fun displayEventDetails(event: Event) {
        Toast.makeText(requireContext(), "Selected ${event.companyName}", Toast.LENGTH_SHORT).show()
    }

    private val recycleListener = RecyclerView.RecyclerListener { holder ->
        val eventHolder = holder as? EventAdapter.EventViewHolder
        eventHolder?.map?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable = null
    }
}
