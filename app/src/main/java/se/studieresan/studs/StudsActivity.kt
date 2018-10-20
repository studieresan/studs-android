package se.studieresan.studs

import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class StudsActivity : AppCompatActivity(), BaseView {

    protected fun addToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        checkNotNull(toolbar) { "Toolbar must not be null" }

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            homePressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    protected fun homePressed() {
        onBackPressed()
    }

    fun setToolbarTitle(title: String) {
        val toolbar = supportActionBar
        checkNotNull(toolbar) { "Can't set title on a non-existent toolbar" }
        toolbar!!.title = title
    }

    fun openBrowser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun setStatusBarColor(@ColorRes color: Int) = window.run {
        statusBarColor = ContextCompat.getColor(this@StudsActivity, color)
    }

    fun setTranslucentStatusBar() = window.run {
        statusBarColor = ContextCompat.getColor(this@StudsActivity, android.R.color.transparent)
    }

    fun setFullscreenLayout() = window.decorView.run {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    val view: View by lazy {
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    }

    override val mainScheduler: Scheduler = AndroidSchedulers.mainThread()
}
