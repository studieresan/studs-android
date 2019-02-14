package se.studieresan.studs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import se.studieresan.studs.LauncherActivity
import se.studieresan.studs.R
import se.studieresan.studs.data.StudsPreferences

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_logout.setOnClickListener { logOut() }
    }

    private fun logOut() {
        StudsPreferences.logOut(requireContext())
        startActivity(LauncherActivity.makeIntent(requireContext()))
        requireActivity().finish()
    }
}
