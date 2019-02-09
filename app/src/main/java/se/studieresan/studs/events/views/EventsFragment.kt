package se.studieresan.studs.events.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.events.adapters.EventAdapter
import se.studieresan.studs.net.StudsRepository
import timber.log.Timber
import javax.inject.Inject

class EventsFragment : Fragment() {

  private var adapter: EventAdapter = EventAdapter()
  private var disposable: Disposable? = null

  @Inject
  lateinit var studsRepository: StudsRepository

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_events, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    StudsApplication.applicationComponent.inject(this)
    fetchEvents()
    swipe_refresh.setOnRefreshListener { fetchEvents() }
    rv_events.layoutManager = LinearLayoutManager(requireContext())
    rv_events.adapter = adapter
  }

  private fun fetchEvents() {
    disposable?.dispose()
    disposable = studsRepository
            .getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { swipe_refresh.isRefreshing = false }
            .subscribe({ adapter.submitList(it.data.allEvents) }, { t -> Timber.d(t) })
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable?.dispose()
    disposable = null
  }
}
