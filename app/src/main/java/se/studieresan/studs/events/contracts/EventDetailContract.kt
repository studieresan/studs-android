package se.studieresan.studs.events.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView

interface EventDetailContract {
    interface View : BaseView {
        fun openGoogleMapsNavigation(address: String)
    }

    interface Presenter : BasePresenter {
        fun didPressCompanyLocation()
        fun didClickPreEventFormButton()
        fun didClickPostEventFormButton()
    }
}
