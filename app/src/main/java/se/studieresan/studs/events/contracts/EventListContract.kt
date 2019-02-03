package se.studieresan.studs.events.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.data.Event

interface EventListContract {

  interface View {
    fun showEventList(events: ArrayList<Event>)
    fun showEventListLoading()
    fun showEventListLoadingError(error: Throwable)
  }

  interface Presenter : BasePresenter {
    fun loadEvents()
  }
}