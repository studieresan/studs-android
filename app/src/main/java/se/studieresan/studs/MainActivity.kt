package se.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import se.studieresan.studs.events.views.EventFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.consume
import se.studieresan.studs.util.inTransaction

class MainActivity : StudsActivity() {

    private lateinit var currentFragment: Fragment

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

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_event -> consume { replaceFragment(EventFragment()) }
                R.id.drawer_trip -> consume { replaceFragment(TripFragment()) }
            }
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }


        // Add hamburger
        addToolbar()
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        if (savedInstanceState == null) {
            replaceFragment(EventFragment())
            drawer.setCheckedItem(R.id.drawer_event)
        } else {
            currentFragment = supportFragmentManager.findFragmentById(FRAGMENT_ID) ?: throw IllegalStateException("No fragment found")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            drawer_layout.openDrawer(GravityCompat.START)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun <F> replaceFragment(fragment: F) where F : Fragment {
        supportFragmentManager.inTransaction {
            currentFragment = fragment
            replace(FRAGMENT_ID, fragment)
        }
    }
}
