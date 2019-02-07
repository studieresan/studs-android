package se.studieresan.studs.data

import android.content.Context
import android.preference.PreferenceManager


private const val LOGGED_IN = "logged_in"
private const val JWT_TOKEN = "jwt_token"

// Abstraction over SharedPrefs
object StudsPreferences {

    fun isLoggedIn(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(LOGGED_IN, false)

    fun logIn(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(LOGGED_IN, true)
            .apply()

    fun logOut(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(LOGGED_IN, false)
            .putString(JWT_TOKEN, "")
            .apply()
    }

    fun setJwtToken(context: Context, token: String) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(JWT_TOKEN, token)
            .apply()

    fun getJwtToken(context: Context) =
        checkNotNull(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(JWT_TOKEN, "")
        )
}
