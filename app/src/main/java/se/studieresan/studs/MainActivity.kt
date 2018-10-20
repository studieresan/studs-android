package se.studieresan.studs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import se.studieresan.studs.events.EventListFragment
import se.studieresan.studs.more.MoreFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.consume
import se.studieresan.studs.util.inTransaction
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

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

    private fun <F> replaceFragment(fragment: F) where F : Fragment {
        supportFragmentManager.inTransaction {
            currentFragment = fragment
            replace(FRAGMENT_ID, fragment)
        }
    }
}
