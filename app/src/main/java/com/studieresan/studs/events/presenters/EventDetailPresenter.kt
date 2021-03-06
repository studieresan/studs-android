package com.studieresan.studs.events.presenters

import io.reactivex.disposables.Disposable
import com.studieresan.studs.events.contracts.EventDetailContract
import com.studieresan.studs.net.StudsRepository

class EventDetailPresenter(
        view: EventDetailContract.View,
        private val event: EventQuery.Event,
) : EventDetailContract.Presenter {

    private var view: EventDetailContract.View? = view

    private var disposable: Disposable? = null


    override fun onCleanup() {
        disposable?.dispose().also {
            disposable = null
        }
        view = null
    }
}
