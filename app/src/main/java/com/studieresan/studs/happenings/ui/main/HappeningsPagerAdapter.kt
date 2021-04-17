package com.studieresan.studs.happenings.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.studieresan.studs.R
import com.studieresan.studs.happenings.MapsFragment
import com.studieresan.studs.trip.TripFragment

private val TAB_TITLES = arrayOf(
        R.string.map_tab,
        R.string.list_tab
)

/**
 * HappeningsPagerAdapter returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class HappeningsPagerAdapter(private val context: FragmentManager?, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page
        return if (position === 0) MapsFragment() else TripFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position === 0) "map" else "list"

        //return context.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}