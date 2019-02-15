package se.studieresan.studs.fcm

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import se.studieresan.studs.MainActivity
import se.studieresan.studs.R
import java.util.concurrent.atomic.AtomicInteger

private const val CHANNEL_NAME = "Notifications"
private const val CHANNEL_DESC = "All notifications related to Studs"

class StudsFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            sendNotification(it)
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val intent = MainActivity.makeIntent(this, true)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.title ?: "")
                .setContentText(notification.body ?: "")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId.getAndIncrement(), notificationBuilder.build())
        }
    }

    companion object {
        @TargetApi(Build.VERSION_CODES.O)
        fun setup(context: Context) {
            val channelId = context.getString(R.string.notification_channel_id)
            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        private val notificationId = AtomicInteger(0)
    }
}
