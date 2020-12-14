package se.studieresan.studs.login.contracts

import se.studieresan.studs.BasePresenter
import se.studieresan.studs.BaseView
import se.studieresan.studs.data.models.LoginResponse

interface LoginContract {

    interface View : BaseView {
        fun showEmailErrorMessage(show: Boolean)
        fun showPasswordErrorMessage(show: Boolean)
        fun showLoginFailedMessage()
        fun loginSuccessful(loginResponse: LoginResponse)
    }

    interface Presenter : BasePresenter {
        fun onLoginClicked(email: String, password: String)
    }
}
