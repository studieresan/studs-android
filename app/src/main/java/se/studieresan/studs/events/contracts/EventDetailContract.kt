package se.studieresan.studs.events.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView
import se.studieresan.studs.data.models.CreatePostEventFormFields
import se.studieresan.studs.data.models.CreatePreEventFormFields
import se.studieresan.studs.data.models.Event

interface EventDetailContract {
    interface View : BaseView {
        fun openGoogleMapsNavigation(address: String)
        fun showPreEventForm(event: Event, formData: CreatePreEventFormFields?)
        fun showPostEventForm(event: Event, formData: CreatePostEventFormFields?)
    }

    interface Presenter : BasePresenter {
        fun didPressCompanyLocation()
        fun didClickPreEventFormButton()
        fun didClickPostEventFormButton()
        fun fetchForms()
    }
}
