package com.studieresan.studs.login.contracts

import com.studieresan.studs.BasePresenter
import com.studieresan.studs.BaseView
import com.studieresan.studs.data.models.LoginResponse

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
