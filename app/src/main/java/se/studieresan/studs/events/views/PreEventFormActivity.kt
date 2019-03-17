package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pre_event_form.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.data.models.Event
import timber.log.Timber

class PreEventFormActivity : StudsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_event_form)
        addToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val event = intent.getParcelableExtra<Event>(IntentExtra.EVENT)
        setToolbarTitle(getString(R.string.pre_event_form_title, event.companyName))
        btn_submit.setOnClickListener { submitForm() }
    }

    private fun validForm(): Boolean {
        var valid = true
        val errorMessage = getString(R.string.required_answer)

        if (rg_familiarity.checkedRadioButtonId == -1) {
            til_familiarity.error = errorMessage
            valid = false
        } else {
            til_familiarity.error = null
        }

        if (rg_interest_in_company.checkedRadioButtonId == -1) {
            til_interest.error = errorMessage
            valid = false
        } else {
            til_interest.error = null
        }

        if (et_motivate_interest.text.isEmpty()) {
            til_motivate_interest.error = errorMessage
            valid = false
        } else {
            til_motivate_interest.error = null
        }

        if (et_describe_view.text.isEmpty()) {
            til_describe_view.error = errorMessage
            valid = false
        } else {
            til_describe_view.error = null
        }

        return valid
    }

    private fun submitForm() {
        if (!validForm()) {
            return
        }
        Timber.d("Valid form")
    }

    companion object {
        fun makeIntent(context: Context, event: Event) = Intent(context, PreEventFormActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }
    }
}
