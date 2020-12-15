package com.studieresan.studs.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.studieresan.studs.R
import com.studieresan.studs.StudsActivity

class SettingsActivity : StudsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        addToolbar()
        setToolbarTitle(getString(R.string.preferences))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment.newInstance())
                .commit()
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
