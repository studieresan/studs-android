package se.studieresan.studs.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.VisibleForTesting


private const val LOGGED_IN = "logged_in"

// Abstraction over SharedPrefs
object StudsPreferences {

    @JvmStatic
    @VisibleForTesting
    @Synchronized
    fun isLoggedIn(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(LOGGED_IN, false)

    fun setIsLoggedIn(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(LOGGED_IN, true)
                    .commit()

    fun setIsLoggedOut(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(LOGGED_IN, false)
                    .commit()
}
