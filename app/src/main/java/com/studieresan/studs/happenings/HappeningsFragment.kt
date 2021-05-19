package com.studieresan.studs.happenings

import HappeningsQuery
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.studieresan.studs.R
import com.studieresan.studs.StudsApplication
import com.studieresan.studs.graphql.apolloClient
import com.studieresan.studs.happenings.adapters.HappeningsPagerAdapter
import com.studieresan.studs.happenings.viewmodels.HappeningsViewModel
import com.studieresan.studs.net.StudsRepository
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_happenings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HappeningsFragment : Fragment() {

    private lateinit var adapter: HappeningsPagerAdapter
    private var disposable: Disposable? = null
    private var viewModel: HappeningsViewModel?=null

    @Inject
    lateinit var studsRepository: StudsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_happenings, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)

        viewModel= ViewModelProviders.of(requireActivity()).get(HappeningsViewModel::class.java)

        btn_create_happening.setOnClickListener {
            val intent = Intent(context, CreateHappeningActivity::class.java)
            startActivity(intent)
        }

        fetchHappenings(false)
        happenings_swipe_refresh.setOnRefreshListener { fetchHappenings(true) }

        // this might be the thing causing it not to reload...
        adapter = HappeningsPagerAdapter(parentFragmentManager, parentFragmentManager)
        happenings_view_pager.adapter = adapter
        happenings_tabs.setupWithViewPager(happenings_view_pager)

    }

    private fun fetchHappenings(refresh: Boolean) {

        CoroutineScope(Dispatchers.Main).launch {
            val response = try {
                apolloClient(requireContext()).query(HappeningsQuery()).await()
            } catch (e: ApolloException) {
                null
            }
            happenings_swipe_refresh.isRefreshing = false
            happenings_progress_bar.visibility = View.GONE

            val happenings = response?.data?.happenings?.filterNotNull()?.sortedByDescending { happening -> happening.created }
            viewModel?.setHappenings(happenings)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose().also {
            disposable = null
        }
    }
}