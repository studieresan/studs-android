package se.studieresan.studs.events

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.studieresan.studs.R

class EventsActivity : AppCompatActivity() {

    companion object {
        fun makeIntent(context: Context, newTask: Boolean = false): Intent {
            val intent = Intent(context, EventsActivity::class.java)
            if (newTask) {
                intent.run {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
    }
}
