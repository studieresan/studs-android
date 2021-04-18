package com.studieresan.studs.happenings.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.studieresan.studs.R
import com.studieresan.studs.happenings.HappeningsListFragment
import com.studieresan.studs.happenings.MapsFragment

private val TAB_TITLES = arrayOf(
        R.string.map_tab,
        R.string.list_tab
)

class HappeningsPagerAdapter(private val context: FragmentManager?, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page
        return if (position === 0) MapsFragment() else HappeningsListFragment()
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