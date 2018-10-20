package se.studieresan.studs.login.presenters

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import se.studieresan.studs.StudsService
import se.studieresan.studs.LoginUserRequest
import se.studieresan.studs.login.contracts.LoginContract
import timber.log.Timber

class LoginPresenter(
        private val view: LoginContract.View,
        private val studsService: StudsService
) : LoginContract.Presenter {

    private var loginDisposable: Disposable? = null

    override fun onLoginClicked(email: String, password: String) {
        val validCredentialFormat = validEmail(email) && validPassword(password)
        view.showEmailErrorMessage(!validEmail(email))
        view.showPasswordErrorMessage(!validPassword(password))

        if (validCredentialFormat) {
            loginDisposable?.dispose()
            loginDisposable = studsService
                    .login(LoginUserRequest(email, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(view.mainScheduler)
                    .subscribe({
                        Timber.d("Authenticated")
                        view.presentMainView()
                    }, {
                        view.showLoginFailedMessage()
                    })
        }
    }

    // Shouldn't technically use Android here in the presenter, but whatever
    private fun validEmail(email: String): Boolean = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String) = password.isNotEmpty()

    override fun onCleanup() {
        loginDisposable?.dispose()
        loginDisposable = null
    }
}
