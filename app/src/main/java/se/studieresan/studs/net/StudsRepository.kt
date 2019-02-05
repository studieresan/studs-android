package se.studieresan.studs.net

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import se.studieresan.studs.data.Events
import se.studieresan.studs.data.ForgotPasswordRequest
import se.studieresan.studs.data.LoginUserRequest
import se.studieresan.studs.data.StudsService
import javax.inject.Inject

class StudsRepository @Inject constructor(private val studsService: StudsService) {

  fun login(email: String, password: String): Observable<ResponseBody> =
      studsService
          .login(LoginUserRequest(email, password))
          .compose(applySchedulers())

  fun forgotPassword(email: String): Observable<ResponseBody> =
      studsService
          .forgotPassword(ForgotPasswordRequest(email))
          .compose(applySchedulers())

  fun getEvents(): Observable<Events> =
      studsService
          .getEvents()
          .compose(applySchedulers())

  private fun <T> applySchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
      it.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }
}
