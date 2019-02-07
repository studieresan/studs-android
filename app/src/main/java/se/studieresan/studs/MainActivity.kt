package se.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.events.views.EventFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.consume
import se.studieresan.studs.util.inTransaction

class MainActivity : StudsActivity() {

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

        // Add hamburger
        addToolbar()
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_event -> consume { replaceFragment(EventFragment()) }
                R.id.drawer_trip -> consume { replaceFragment(TripFragment()) }
                R.id.drawer_logout -> logOut()
            }
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }

        // If we don't have a current Fragment from the bundle, jump to Events
        if (savedInstanceState == null) {
            replaceFragment(EventFragment())
            drawer.setCheckedItem(R.id.drawer_event)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun logOut() {
        StudsPreferences.logOut(this)

        startActivity(LauncherActivity.makeIntent(this))
        finish()
    }

    private fun <F> replaceFragment(fragment: F) where F : Fragment {
        supportFragmentManager.inTransaction {
            replace(FRAGMENT_ID, fragment)
        }
    }
}
