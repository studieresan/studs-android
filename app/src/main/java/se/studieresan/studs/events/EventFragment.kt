package se.studieresan.studs.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import se.studieresan.studs.R

class EventFragment : Fragment() {

    private lateinit var mPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPager = view.findViewById(R.id.event_pager) as ViewPager
        val tabs = view.findViewById(R.id.event_tabs) as TabLayout
        tabs.setupWithViewPager(mPager)
        mPager.adapter = EventPagerAdapter(childFragmentManager)
    }

    private inner class EventPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return UpcomingFragment()
                1 -> return PastFragment()
            }
            throw IndexOutOfBoundsException("Unknown tab: $position")
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return resources.getString(R.string.upcoming)
                1 -> return resources.getString(R.string.past)
            }
            throw IndexOutOfBoundsException("Unknown tab: $position")
        }
    }

}
