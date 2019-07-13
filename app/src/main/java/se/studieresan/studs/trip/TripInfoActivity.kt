package se.studieresan.studs.trip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_trip_info.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.IntentExtra
import timber.log.Timber

class TripInfoActivity : StudsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_info)
        addToolbar()

        val infoType = intent.getSerializableExtra(IntentExtra.TRIP_INFO_TYPE)

        setToolbarTitle(
            if (infoType == InfoType.HOUSING)
                getString(R.string.housing)
            else
                getString(R.string.contacts)
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (infoType == InfoType.HOUSING) {
            setupHousingListener()
        } else {
            setupContactsListener()
        }
    }

    private fun setupContactsListener() {
        FirebaseDatabase.getInstance().getReference("contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Timber.w(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    tv_info.text = p0.value as String
                }
            })
    }

    private fun setupHousingListener() {
        FirebaseDatabase.getInstance().getReference("housing")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Timber.w(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    tv_info.text = p0.value as String
                }
            })
    }

    companion object {
        enum class InfoType {
            HOUSING,
            CONTACTS,
        }

        fun makeIntent(context: Context, infoType: InfoType) = Intent(context, TripInfoActivity::class.java).apply {
            putExtra(IntentExtra.TRIP_INFO_TYPE, infoType)
        }
    }
}
