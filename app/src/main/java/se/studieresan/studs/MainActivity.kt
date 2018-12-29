package se.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.events.views.EventFragment
import se.studieresan.studs.trip.TripFragment
import se.studieresan.studs.util.inTransaction
import timber.log.Timber

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

  private var disposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(main_toolbar)
    bottom_navigation.setOnNavigationItemSelectedListener(navItemSelectListener)

    // If we don't have a current Fragment from the bundle, jump to Events
    if (savedInstanceState == null) {
      replaceFragment(EventFragment())
      bottom_navigation.selectedItemId = R.id.navigation_events
    }

    // Just for testing
    // val service = (application as StudsApplication).studsService
    // disposable = service.getEvents()
    //     .subscribeOn(Schedulers.io())
    //     .subscribe({ events -> Timber.d(events.toString()) }, { err -> Timber.e(err) })
  }

  private val navItemSelectListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_events -> {
        main_toolbar.title = "Events"
        replaceFragment(EventFragment())
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_trip -> {
        main_toolbar.title = "Trip"
        replaceFragment(TripFragment())
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_profile -> {
        main_toolbar.title = "Profile"
        return@OnNavigationItemSelectedListener true
      }
    }
    false
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

  override fun onDestroy() {
    super.onDestroy()
    disposable?.dispose()
    disposable = null
  }
}
