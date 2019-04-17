package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_event_check_in.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.events.adapters.CheckInAdapter
import timber.log.Timber

class EventCheckInActivity : StudsActivity() {

    private lateinit var adapter: CheckInAdapter
    private var ref: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_check_in)
        addToolbar()
        val event = intent.getParcelableExtra<Event>(IntentExtra.EVENT)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setToolbarTitle("Check In for ${event.companyName}")
        adapter = CheckInAdapter(emptyArray())
        recycler_view.run {
            layoutManager = LinearLayoutManager(this@EventCheckInActivity)
            adapter = this@EventCheckInActivity.adapter
            addItemDecoration(DividerItemDecoration(this@EventCheckInActivity, RecyclerView.VERTICAL))
        }

        fab_check_in.setOnClickListener {
            checkInUser()
        }

        ref = FirebaseDatabase.getInstance().getReference(event.companyName)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val checkedInUsers = dataSnapshot.child("check-ins")
                    .children
                    .map { it.value as String }
                    .sortedBy { it }
                    .toTypedArray()
                adapter.setItems(checkedInUsers)
            }

            override fun onCancelled(err: DatabaseError) {
                Timber.e("Failed to read value. ${err.toException()}")
            }
        })
    }

    private fun checkInUser() {
        ref!!.child("check-ins").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Timber.e("Failed to read value. ${p0.toException()}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.children.toList().any { it.key == "" }) {
                    ref!!.child("check-ins").child("").setValue(null)
                } else {
                    ref!!.child("check-ins").child("").setValue("")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ref = null
    }

    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, EventCheckInActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }
    }
}
