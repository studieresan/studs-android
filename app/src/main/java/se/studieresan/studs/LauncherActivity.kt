package se.studieresan.studs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.login.views.ForgotPasswordActivity
import se.studieresan.studs.login.views.LoginActivity

class LauncherActivity : AppCompatActivity() {

    companion object {
        fun makeIntent(context: Context) = Intent(context, LauncherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (StudsPreferences.isLoggedIn(this))
            startActivity(MainActivity.makeIntent(this, true))
        else
            startActivity(LoginActivity.makeIntent(this))
        finish()
    }
}
