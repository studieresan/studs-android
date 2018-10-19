package se.studieresan.studs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.studieresan.studs.login.views.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goToLogin()
    }

    private fun goToLogin() = startActivity(LoginActivity.makeIntent(this))
}
