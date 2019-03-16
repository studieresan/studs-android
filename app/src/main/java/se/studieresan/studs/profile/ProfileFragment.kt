package se.studieresan.studs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_profile.*
import se.studieresan.studs.LauncherActivity
import se.studieresan.studs.R
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.notifications.PushNotificationsActivity
import se.studieresan.studs.util.RemoteConfigKeys

class ProfileFragment : Fragment() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        btn_send_notification.setOnClickListener { goToPushNotificationsView() }
        btn_logout.setOnClickListener { logOut() }

        btn_send_notification.visibility = if (remoteConfig.getBoolean(RemoteConfigKeys.SEND_NOTIFICATIONS_ENABLED)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun goToPushNotificationsView() = startActivity(PushNotificationsActivity.makeIntent(requireContext()))

    private fun logOut() {
        StudsPreferences.logOut(requireContext())
        startActivity(LauncherActivity.makeIntent(requireContext()))
        requireActivity().finish()
    }
}
