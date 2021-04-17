package com.studieresan.studs.happenings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studieresan.studs.R
import com.studieresan.studs.happenings.ui.main.HappeningsPagerAdapter
import kotlinx.android.synthetic.main.fragment_happenings.*


class HappeningsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val happeningsPagerAdapter = HappeningsPagerAdapter(null, parentFragmentManager)
        happenings_view_pager.adapter = happeningsPagerAdapter
        happenings_tabs.setupWithViewPager(happenings_view_pager)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_happenings, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}