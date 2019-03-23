package se.studieresan.studs.net

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import se.studieresan.studs.data.StudsService
import se.studieresan.studs.data.models.CompanyFamiliarity
import se.studieresan.studs.data.models.CreatePostEventFormBody
import se.studieresan.studs.data.models.CreatePostEventFormFields
import se.studieresan.studs.data.models.CreatePostEventFormRequest
import se.studieresan.studs.data.models.CreatePreEventFormBody
import se.studieresan.studs.data.models.CreatePreEventFormFields
import se.studieresan.studs.data.models.CreatePreEventFormRequest
import se.studieresan.studs.data.models.EventForms
import se.studieresan.studs.data.models.EventImpact
import se.studieresan.studs.data.models.Events
import se.studieresan.studs.data.models.ForgotPasswordRequest
import se.studieresan.studs.data.models.LoginUserRequest
import se.studieresan.studs.data.models.User
import se.studieresan.studs.util.GraphQLHelper
import javax.inject.Inject

class StudsRepository @Inject constructor(private val studsService: StudsService) {

    fun login(email: String, password: String): Observable<ResponseBody> =
        studsService
            .login(LoginUserRequest(email, password))
            .compose(applySchedulers())

    fun getUser(): Observable<User> =
        studsService
            .getUser()
            .map { it.data.user }
            .compose(applySchedulers())

    fun forgotPassword(email: String): Observable<ResponseBody> =
        studsService
            .forgotPassword(ForgotPasswordRequest(email))
            .compose(applySchedulers())

    fun getEvents(): Observable<Events> =
        studsService
            .getEvents()
            .compose(applySchedulers())

    fun postPreEventForm(
        eventId: String,
        interestInRegularWork: Int,
        viewOfCompany: String,
        interestInCompanyMotivation: String,
        familiarity: CompanyFamiliarity
    ): Observable<Any> =
        studsService
            .postPreEventForm(
                CreatePreEventFormRequest(
                    CreatePreEventFormBody(
                        eventId,
                        CreatePreEventFormFields(
                            interestInRegularWork,
                            viewOfCompany,
                            interestInCompanyMotivation,
                            familiarity
                        )
                    )
                )
            )
            .andThen(Observable.just(Any()))
            .compose(applySchedulers())

    fun postPostEventForm(
        eventId: String,
        interestInRegularWork: Int,
        interestInCompanyMotivation: String,
        eventImprovements: String,
        eventFeedback: String,
        foodRating: Int,
        activitiesRating: Int,
        atmosphereRating: Int,
        qualifiedToWork: Boolean,
        eventImpact: EventImpact
    ): Observable<Any> =
        studsService
            .postPostEventForm(
                CreatePostEventFormRequest(
                    CreatePostEventFormBody(
                        eventId,
                        CreatePostEventFormFields(
                            interestInRegularWork,
                            interestInCompanyMotivation,
                            eventImprovements,
                            eventFeedback,
                            foodRating,
                            activitiesRating,
                            atmosphereRating,
                            qualifiedToWork,
                            eventImpact
                        )
                    )
                )
            )
            .andThen(Observable.just(Any()))
            .compose(applySchedulers())

    fun getFormsForEvent(userId: String, eventId: String): Observable<EventForms> {
        return Observable.fromCallable { GraphQLHelper.getFormsQuery(userId, eventId) }
            .switchMap { studsService.getFormsForEvent(it).compose(applySchedulers()) }
    }

    private fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
