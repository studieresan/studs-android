package se.studieresan.studs.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

        configureView()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        tv_preferences.setOnClickListener {
            requireActivity().startActivity(SettingsActivity.makeIntent(requireContext()))
        }
    }

    private fun configureView() {
        // Set the image
        Glide.with(requireContext())
                .load(StudsPreferences.getPicture(requireContext()))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

        // Set the name
        tv_member_name.text = StudsPreferences.getName(requireContext())

        // Set the position
        tv_member_position.text = StudsPreferences.getPosition(requireContext())
    }

    private fun logOut() {
        StudsPreferences.logOut(requireContext())
        startActivity(LauncherActivity.makeIntent(requireContext()))
        requireActivity().finish()
    }
}
