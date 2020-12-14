package se.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import se.studieresan.studs.events.views.EventsFragment
import se.studieresan.studs.profile.ProfileFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.inTransaction
import androidx.appcompat.app.AppCompatDelegate
import se.studieresan.studs.data.StudsPreferences

private const val TOPIC_ALL = "all"

class MainActivity : StudsActivity() {

    private var currentFragmentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addToolbar()
        bottom_navigation.setOnNavigationItemSelectedListener(navItemSelectListener)

        // If we don't have a current Fragment from the bundle, jump to Events
        if (savedInstanceState == null) {
            replaceFragment(R.id.navigation_events, EventsFragment())
            setToolbarTitle(R.string.event)
        }

        if (StudsPreferences.getDarkModePreferences(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    private val navItemSelectListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (currentFragmentId == item.itemId) {
            return@OnNavigationItemSelectedListener true
        }

        when (item.itemId) {
            R.id.navigation_events -> {
                setToolbarTitle(R.string.event)
                replaceFragment(R.id.navigation_events, EventsFragment())
            }
            R.id.navigation_trip -> {
                setToolbarTitle(R.string.trip)
                replaceFragment(R.id.navigation_trip, TripFragment())
            }
            R.id.navigation_profile -> {
                setToolbarTitle(R.string.profile)
                replaceFragment(R.id.navigation_profile, ProfileFragment())
            }
        }

        return@OnNavigationItemSelectedListener true
    }

    private fun <F> replaceFragment(id: Int, fragment: F) where F : Fragment {
        currentFragmentId = id

        supportFragmentManager.inTransaction {
            replace(FRAGMENT_ID, fragment)
        }
    }

    companion object {
        fun makeIntent(context: Context, newTask: Boolean = false): Intent {
            val intent = Intent(context, MainActivity::class.java)
            if (newTask) {
                intent.run {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            return intent
        }

        private const val FRAGMENT_ID = R.id.fragment_container
    }

}
