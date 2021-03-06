package com.studieresan.studs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.studieresan.studs.data.StudsPreferences
import com.studieresan.studs.login.views.LoginActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)

        if (StudsPreferences.getDarkModePreferences(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (StudsPreferences.isLoggedIn(this))
            startActivity(MainActivity.makeIntent(this))
        else
            startActivity(LoginActivity.makeIntent(this))
        finish()
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, LauncherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
