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
        configureView()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        tv_notifications.setOnClickListener {
            val intent = Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.run {
                    putExtra("android.provider.extra.APP_PACKAGE", requireActivity().packageName)
                }

            } else {
                intent.run {
                    putExtra("app_package", requireActivity().packageName)
                    putExtra("app_uid", requireActivity().applicationInfo.uid)
                }
            }

            requireActivity().startActivity(intent)
        }
    }

    private fun goToPushNotificationsView() = startActivity(PushNotificationsActivity.makeIntent(requireContext()))

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
