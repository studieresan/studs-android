package se.studieresan.studs

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import se.studieresan.studs.R.id.toolbar
import se.studieresan.studs.events.EventFragment
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

        drawer.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            true
        }

        replaceFragment(EventFragment())
        drawer.setCheckedItem(R.id.drawer_event)

        // Add hamburger
        addToolbar()
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_events -> consume { replaceFragment(EventListFragment()) }
                R.id.navigation_trip -> consume { replaceFragment(TripFragment()) }
                R.id.navigation_more -> consume { replaceFragment(MoreFragment()) }
                else -> throw IllegalStateException("Unknown item id")
            }
        }

        // Consume event
        navigation.setOnNavigationItemReselectedListener {  }

        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.navigation_events
            replaceFragment(EventListFragment())
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
