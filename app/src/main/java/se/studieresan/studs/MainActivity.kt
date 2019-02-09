package se.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.events.views.EventsFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.inTransaction

class MainActivity : StudsActivity() {

    private var currentFragmentId = 0

    private val mapTabToFragment = mapOf(
            R.id.navigation_events to EventsFragment(),
            R.id.navigation_trip to TripFragment(),
            R.id.navigation_profile to TripFragment()
    )

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addToolbar()
        bottom_navigation.setOnNavigationItemSelectedListener(navItemSelectListener)

        // If we don't have a current Fragment from the bundle, jump to Events
        if (savedInstanceState == null) {
            replaceFragment(R.id.navigation_events)
        }
    }

    private val navItemSelectListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (currentFragmentId == item.itemId) {
            return@OnNavigationItemSelectedListener true
        }

        when (item.itemId) {
            R.id.navigation_events -> {
                toolbar.title = getString(R.string.event)
                replaceFragment(R.id.navigation_events)
            }
            R.id.navigation_trip -> {
                toolbar.title = getString(R.string.trip)
                replaceFragment(R.id.navigation_trip)
            }
            R.id.navigation_profile -> {
                toolbar.title = getString(R.string.profile)
                replaceFragment(R.id.navigation_profile)
            }
        }

        return@OnNavigationItemSelectedListener true
    }

    private fun logOut() {
        StudsPreferences.logOut(this)

        startActivity(LauncherActivity.makeIntent(this))
        finish()
    }

    private fun replaceFragment(@IdRes id: Int) {
        currentFragmentId = id

        supportFragmentManager.inTransaction {
            replace(FRAGMENT_ID, mapTabToFragment[id]!!)
        }
    }
}
