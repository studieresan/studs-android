package se.studieresan.studs

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.studieresan.studs.data.AddJwtInterceptor
import se.studieresan.studs.data.ReceivedJwtInterceptor
import se.studieresan.studs.data.StudsService
import timber.log.Timber

private const val STUDS_URL = "https://studs18-overlord.herokuapp.com/"

class StudsApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      // Plant Timber
      Timber.plant(Timber.DebugTree())
    } else {
      // Setup Instabug for feedback and bugs
      Instabug.Builder(this, BuildConfig.INSTABUG_KEY)
          .setInvocationEvents(InstabugInvocationEvent.SCREENSHOT)
          .build()
    }
  }

  private val retrofit by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(AddJwtInterceptor(this))
        .addInterceptor(ReceivedJwtInterceptor(this))
        .build()

    Retrofit.Builder()
        .baseUrl(STUDS_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
  }

  val studsService: StudsService by lazy {
    retrofit.create(StudsService::class.java)
  }
}
