package com.studieresan.studs.net

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import com.studieresan.studs.data.StudsService
import com.studieresan.studs.data.models.*
import javax.inject.Inject

class StudsRepository @Inject constructor(private val studsService: StudsService) {

    private fun happeningCreateMutation(happening: HappeningInput): String {
        return """mutation {
    happeningCreate(fields: {
            host: ${happening.host}
            participants: ${happening.participants}
            location: ${happening.location}
            title: ${happening.title}
            emoji: ${happening.emoji}
            description: ${happening.description}
           
        })
    }"""
    }

    fun login(email: String, password: String): Observable<LoginResponse> =
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

    fun getHappenings(): Observable<Happenings> =
            studsService
                    .getHappenings()
                    .compose(applySchedulers())

    fun createHappening(happening: HappeningInput): Observable<Happening> =
            studsService
                    .createHappening(happening)
                    .compose(applySchedulers())

    private fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
