package se.studieresan.studs.login.presenters

import se.studieresan.studs.login.contracts.LoginContract

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {
    override fun onLoginClicked(email: String, password: String) {
        val validCredentialFormat = validEmail(email) && validPassword(password)
        view.showEmailErrorMessage(!validEmail(email))
        view.showPasswordErrorMessage(!validPassword(password))

        if (validCredentialFormat) {
            // Todo, login here
            view.showLoginFailedMessage()
        }
    }

    // Shouldn't technically use Android here in the presenter, but whatever
    private fun validEmail(email: String): Boolean = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String) = password.length > 6

    override fun onCleanup() {
    }
}