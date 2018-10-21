package se.studieresan.studs.login.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.login.contracts.LoginContract
import se.studieresan.studs.login.presenters.LoginPresenter

class LoginActivity : StudsActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    companion object {
        fun makeIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupClickListeners()
        val studsService = (application as StudsApplication).studsService
        presenter = LoginPresenter(this, studsService)
    }

    private fun setupClickListeners() {
        btn_login.setOnClickListener { login() }

        btn_forgot_password.setOnClickListener { showForgotPassword() }
    }

    private fun showForgotPassword() {
        startActivity(ForgotPasswordActivity.makeIntent(this, et_email.text.toString()))
    }

    private fun login() = presenter.onLoginClicked(et_email.text.toString(), et_password.text.toString())

    override fun showEmailErrorMessage(show: Boolean) {
        til_email.error = if (show) getString(R.string.invalid_email) else null
    }

    override fun showPasswordErrorMessage(show: Boolean) {
        til_password.error = if (show) getString(R.string.invalid_password) else null
    }

    override fun showLoginFailedMessage() {
        btn_login.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
        Snackbar.make(view, getString(R.string.invalid_email_or_password), Snackbar.LENGTH_SHORT).show()
    }

    override fun presentMainView() {
        // todo, present event view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onCleanup()
    }
}
