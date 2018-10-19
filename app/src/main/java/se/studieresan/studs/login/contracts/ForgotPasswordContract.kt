package se.studieresan.studs.login.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView

interface ForgotPasswordContract {

    interface View : BaseView {
        fun showEmailSentVerification()
        fun showGenericErrorMessage()
        fun showInvalidEmailError()
    }

    interface Presenter : BasePresenter {
        fun forgotEmail(email: String)
    }
}