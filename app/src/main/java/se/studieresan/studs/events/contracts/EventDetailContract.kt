package se.studieresan.studs.events.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView
import se.studieresan.studs.data.Address

interface EventDetailContract {
    interface View : BaseView {
        fun openGoogleMapsNavigation(address: Address)
    }

    interface Presenter : BasePresenter {
        fun didPressCompanyLocation()
        fun didClickPreEventFormButton()
        fun didClickPostEventFormButton()
    }
}
