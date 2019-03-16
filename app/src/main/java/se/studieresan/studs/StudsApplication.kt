package se.studieresan.studs

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.jakewharton.threetenabp.AndroidThreeTen
import se.studieresan.studs.di.*
import timber.log.Timber

private const val STUDS_URL = "https://studs18-overlord.herokuapp.com/"

class StudsApplication : Application() {

    companion object {
        lateinit var applicationComponent: StudsApplicationComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            // Plant Timber
            Timber.plant(Timber.DebugTree())
        } else {
            // Setup Instabug for feedback and bugs
            Instabug.Builder(this, BuildConfig.INSTABUG_KEY)
                .setInvocationEvents(InstabugInvocationEvent.SCREENSHOT)
                .build()
        }

        applicationComponent = createApplicationComponent()
    }

    private fun createApplicationComponent(): StudsApplicationComponent {
        return DaggerStudsApplicationComponent
            .builder()
            .appModule(AppModule(this))
            .netModule(NetModule(STUDS_URL))
            .serviceModule(ServiceModule())
            .build()
    }
}
