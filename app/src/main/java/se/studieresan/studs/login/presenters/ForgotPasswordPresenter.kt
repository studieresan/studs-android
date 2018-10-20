package se.studieresan.studs.login.presenters

import se.studieresan.studs.login.contracts.ForgotPasswordContract

class ForgotPasswordPresenter(
        private val view: ForgotPasswordContract.View
) : ForgotPasswordContract.Presenter {
    override fun forgotEmail(email: String) {
        // todo, reset in backend
        when {
            email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> view.showEmailSentVerification()
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> view.showInvalidEmailError()
            else -> view.showInvalidEmailError()
        }
    }

    override fun onCleanup() {
    }
}