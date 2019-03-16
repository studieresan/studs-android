package se.studieresan.studs.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_push_notifications.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.FirebaseService
import se.studieresan.studs.data.models.FirebaseRequest

private const val SERVER_KEY = "key"
private const val BASE_URL = "https://fcm.googleapis.com/fcm/"

class PushNotificationsActivity : StudsActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_notifications)
        addToolbar()
        val toolbar = requireNotNull(supportActionBar)
        toolbar.setDisplayHomeAsUpEnabled(true)
        setUpClickListener()
    }

    private val okHttpClient by lazy {
        OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val firebaseService by lazy {
        retrofit.create(FirebaseService::class.java)
    }

    private fun setUpClickListener() {
        btn_send.setOnClickListener {
            sendNotification(et_notification_title.text.toString(), et_notification_message.text.toString())
        }
    }

    private fun sendNotification(title: String, message: String) {
        if (message.isEmpty()) {
            til_notification_message.error = "You cannot send a blank message"
            return
        }

        til_notification_message.error = null

        disposable?.dispose()
        disposable = firebaseService
                .sendNotificationMessage(SERVER_KEY, FirebaseRequest(notification = mapOf(
                        "title" to title,
                        "text" to message))
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose().also {
            disposable = null
        }
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, PushNotificationsActivity::class.java)
    }
}
