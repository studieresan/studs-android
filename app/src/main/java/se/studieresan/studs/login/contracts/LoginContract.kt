package se.studieresan.studs.login.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView

interface LoginContract {

    interface View : BaseView {
        fun showEmailErrorMessage(show: Boolean)
        fun showPasswordErrorMessage(show: Boolean)
        fun showLoginFailedMessage()
        fun presentMainView()
    }

    interface Presenter : BasePresenter {
        fun onLoginClicked(email: String, password: String)
    }
}