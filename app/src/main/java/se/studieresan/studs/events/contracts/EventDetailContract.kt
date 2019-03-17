package se.studieresan.studs.events.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView
import se.studieresan.studs.data.models.Event

interface EventDetailContract {
    interface View : BaseView {
        fun openGoogleMapsNavigation(address: String)
        fun showPreEventForm(event: Event)
        fun showPostEventForm(event: Event)
    }

    interface Presenter : BasePresenter {
        fun didPressCompanyLocation()
        fun didClickPreEventFormButton()
        fun didClickPostEventFormButton()
    }
}
