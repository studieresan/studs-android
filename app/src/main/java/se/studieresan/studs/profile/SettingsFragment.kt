package se.studieresan.studs.profile


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import se.studieresan.studs.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
