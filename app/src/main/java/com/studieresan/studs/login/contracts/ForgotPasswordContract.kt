package com.studieresan.studs.login.contracts

import com.studieresan.studs.BasePresenter
import com.studieresan.studs.BaseView

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
