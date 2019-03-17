package se.studieresan.studs.events.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_post_event_form.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.data.models.Event
import timber.log.Timber

class PostEventFormActivity : StudsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_event_form)

        addToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val event = intent.getParcelableExtra<Event>(IntentExtra.EVENT)
        setToolbarTitle(getString(R.string.after_event_form_title, event.companyName))
        btn_submit.setOnClickListener { submitForm() }
    }

    private fun validForm(): Boolean {
        var valid = true
        val errorMessage = getString(R.string.required_answer)

        if (rg_impact.checkedRadioButtonId == -1) {
            til_impact.error = errorMessage
            valid = false
        } else {
            til_impact.error = null
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

        if (rg_qualified.checkedRadioButtonId == -1) {
            til_qualified_to_work.error = errorMessage
            valid = false
        } else {
            til_qualified_to_work.error = null
        }

        if (rg_atmosphere.checkedRadioButtonId == -1) {
            til_atmosphere.error = errorMessage
            valid = false
        } else {
            til_atmosphere.error = null
        }

        if (rg_activities.checkedRadioButtonId == -1) {
            til_activities.error = errorMessage
            valid = false
        } else {
            til_activities.error = null
        }

        if (rg_food.checkedRadioButtonId == -1) {
            til_food.error = errorMessage
            valid = false
        } else {
            til_food.error = null
        }

        if (et_enjoyed.text.isEmpty()) {
            til_enjoyed.error = errorMessage
            valid = false
        } else {
            til_enjoyed.error = null
        }

        if (et_improved.text.isEmpty()) {
            til_improved.error = errorMessage
            valid = false
        } else {
            til_improved.error = null
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
        fun makeIntent(context: Context, event: Event) = Intent(context, PostEventFormActivity::class.java).apply {
            putExtra(IntentExtra.EVENT, event)
        }
    }
}
