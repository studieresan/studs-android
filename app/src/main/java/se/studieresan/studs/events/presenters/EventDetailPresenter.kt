package se.studieresan.studs.events.presenters

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.disposables.Disposable
import se.studieresan.studs.data.models.CreatePostEventFormFields
import se.studieresan.studs.data.models.CreatePreEventFormFields
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.events.contracts.EventDetailContract
import se.studieresan.studs.net.StudsRepository
import se.studieresan.studs.util.RemoteConfigKeys

class EventDetailPresenter(
    view: EventDetailContract.View,
    private val event: Event,
    remoteConfig: FirebaseRemoteConfig,
    private val studsRepository: StudsRepository
) : EventDetailContract.Presenter {
    private var view: EventDetailContract.View? = view

    private var disposable: Disposable? = null

    private val inAppFormsEnabled = remoteConfig.getBoolean(RemoteConfigKeys.IN_APP_FORMS_ENABLED)

    private var preEventFormData: CreatePreEventFormFields? = null
    private var postEventFormData: CreatePostEventFormFields? = null

    override fun didPressCompanyLocation() = view!!.openGoogleMapsNavigation(event.location)

    override fun didClickPreEventFormButton() {
        if (inAppFormsEnabled) {
            view!!.showPreEventForm(event, preEventFormData)
        } else {
            event.getPreEventForm()?.let { form ->
                view!!.openBrowser(form)
            }
        }
    }

    override fun didClickPostEventFormButton() {
        if (inAppFormsEnabled) {
            view!!.showPostEventForm(event, postEventFormData)
        } else {
            event.getPostEventForm()?.let { form ->
                view!!.openBrowser(form)
            }
        }
    }

    override fun fetchForms() {
        if (inAppFormsEnabled) {
            disposable?.dispose()
            disposable = studsRepository.getUser()
                .switchMap {
                    studsRepository.getFormsForEvent(it.id, event.id)
                        .subscribeOn(view!!.ioScheduler)
                }
                .subscribeOn(view!!.ioScheduler)
                .observeOn(view!!.mainScheduler)
                .subscribe {
                    val eventForms = it.data.eventForms

                    if (eventForms.size == 2) {
                        val preEventForm = eventForms[0]
                        val postEventForm = eventForms[1]
                        preEventFormData = CreatePreEventFormFields(
                            preEventForm.interestInRegularWorkBefore!!,
                            preEventForm.viewOfCompany!!,
                            preEventForm.interestInCompanyMotivationBefore!!,
                            preEventForm.familiarWithCompany!!
                        )
                        postEventFormData = CreatePostEventFormFields(
                            postEventForm.interestInRegularWork!!,
                            postEventForm.interestInCompanyMotivation!!,
                            postEventForm.eventImprovements!!,
                            postEventForm.eventFeedback!!,
                            postEventForm.foodRating!!,
                            postEventForm.activitiesRating!!,
                            postEventForm.atmosphereRating!!,
                            postEventForm.qualifiedToWork!!,
                            postEventForm.eventImpact!!
                        )
                    } else if (eventForms.size == 1) {
                        // We only have a pre or post event form, not both
                        val form = eventForms.first()
                        if (form.foodRating != null) {
                            // Post event form
                            postEventFormData = CreatePostEventFormFields(
                                form.interestInRegularWork!!,
                                form.interestInCompanyMotivation!!,
                                form.eventImprovements!!,
                                form.eventFeedback!!,
                                form.foodRating,
                                form.activitiesRating!!,
                                form.atmosphereRating!!,
                                form.qualifiedToWork!!,
                                form.eventImpact!!
                            )
                        } else {
                            // Pre event form
                            preEventFormData = CreatePreEventFormFields(
                                form.interestInRegularWorkBefore!!,
                                form.viewOfCompany!!,
                                form.interestInCompanyMotivationBefore!!,
                                form.familiarWithCompany!!
                            )
                        }
                    }
                }
        }
    }

    override fun onCleanup() {
        disposable?.dispose().also {
            disposable = null
        }
        view = null
    }
}
