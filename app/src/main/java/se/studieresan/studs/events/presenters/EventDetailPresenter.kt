package se.studieresan.studs.events.presenters

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.events.contracts.EventDetailContract
import se.studieresan.studs.util.RemoteConfigKeys

class EventDetailPresenter(
        view: EventDetailContract.View,
        private val event: Event,
        remoteConfig: FirebaseRemoteConfig
) : EventDetailContract.Presenter {
    private var view: EventDetailContract.View? = view

    private val inAppFormsEnabled = remoteConfig.getBoolean(RemoteConfigKeys.IN_APP_FORMS_ENABLED)

    override fun didPressCompanyLocation() = view!!.openGoogleMapsNavigation(event.location)

    override fun didClickPreEventFormButton() {
        event.getPreEventForm()?.let { form ->
            if (inAppFormsEnabled) {
                view!!.showPreEventForm(event)
            } else {
                view!!.openBrowser(form)
            }
        }
    }

    override fun didClickPostEventFormButton() {
        event.getPostEventForm()?.let { form ->
            if (inAppFormsEnabled) {
                view!!.showPostEventForm(event)
            } else {
                view!!.openBrowser(form)
            }
        }
    }

    override fun onCleanup() {
        view = null
    }
}
