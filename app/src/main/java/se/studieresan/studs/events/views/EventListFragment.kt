package se.studieresan.studs.events.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.studieresan.studs.R
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.data.Event
import se.studieresan.studs.events.adapters.EventListAdapter
import se.studieresan.studs.events.contracts.EventListContract
import se.studieresan.studs.events.presenters.EventListPresenter
import timber.log.Timber

class EventListFragment : Fragment(), EventListContract.View {

  private lateinit var rootView: View
  private lateinit var presenter: EventListContract.Presenter
  private lateinit var viewAdapter: EventListAdapter
  private var events: ArrayList<Event> = ArrayList()

  private lateinit var recyclerView: RecyclerView
  private lateinit var loading: ProgressBar

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_event_list, container, false)

    recyclerView = rootView.findViewById(R.id.recycler_view_events)
    loading = rootView.findViewById(R.id.progressBar)

    val studsService = (activity?.application as StudsApplication).studsService
    presenter = EventListPresenter(this, studsService)

    viewAdapter = EventListAdapter(events)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = viewAdapter

    // Fetch the events from the server
    presenter.loadEvents()

    return rootView
  }

  override fun showEventList(events: ArrayList<Event>) {
    loading.visibility = View.INVISIBLE
    recyclerView.visibility = View.VISIBLE

    this.events.clear()
    this.events.addAll(events)
    viewAdapter.notifyDataSetChanged()
  }

  override fun showEventListLoading() {
    recyclerView.visibility = View.INVISIBLE
    loading.visibility = View.VISIBLE
  }

  override fun showEventListLoadingError(error: Throwable) {
    Timber.d("showEventListLoadingError")
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onCleanup()
  }
}