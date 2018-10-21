package se.studieresan.studs.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_event.*
import se.studieresan.studs.R

class EventFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_tabs.setupWithViewPager(event_pager)
        event_pager.adapter = EventPagerAdapter(childFragmentManager)
    }

    private inner class EventPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return UpcomingEventsFragment()
                1 -> return PastEventsFragment()
            }
            throw IndexOutOfBoundsException("Unknown tab: $position")
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return resources.getString(R.string.upcoming)
                1 -> return resources.getString(R.string.past)
            }
            throw IndexOutOfBoundsException("Unknown tab: $position")
        }
    }

}
