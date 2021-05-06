package com.studieresan.studs.events.views

import EventsQuery
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.studieresan.studs.R
import com.studieresan.studs.StudsApplication
import com.studieresan.studs.events.adapters.EventAdapter
import com.studieresan.studs.graphql.apolloClient
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : Fragment() {

    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)
        progress_bar.visibility = View.VISIBLE
        fetchEvents(false)
        swipe_refresh.setOnRefreshListener { fetchEvents(true) }
    }

    private fun fetchEvents(refresh: Boolean) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient(requireContext()).query(EventsQuery()).await()
            } catch (e: ApolloException) {
                null
            }

            val events = response?.data?.events?.filterNotNull()

            progress_bar.visibility = View.GONE

            if (events != null && !response.hasErrors()) {
                if (!refresh) {
                    adapter = EventAdapter(events, requireActivity().applicationContext) { event -> displayEventDetails(event.id) }
                    rv_events.run {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@EventsFragment.adapter
                        setHasFixedSize(true)
                        setRecyclerListener(recycleListener)
                    }
                }
                swipe_refresh.isRefreshing = false
            }
        }
    }

    private fun displayEventDetails(eventID: String?) =
            startActivity(EventDetailActivity.makeIntent(requireContext(), if (eventID is String) eventID else ""))

    private val recycleListener = RecyclerView.RecyclerListener { holder ->
        val eventHolder = holder as? EventAdapter.NextEventViewHolder
    }

}
