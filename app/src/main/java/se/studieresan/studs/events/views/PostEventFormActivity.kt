package se.studieresan.studs.events.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_post_event_form.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.data.models.CreatePostEventFormFields
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.data.models.EventImpact
import se.studieresan.studs.net.StudsRepository
import javax.inject.Inject

class PostEventFormActivity : StudsActivity() {

    @Inject
    lateinit var studsRepository: StudsRepository

    private lateinit var event: Event

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)
        setContentView(R.layout.activity_post_event_form)

        addToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        event = intent.getParcelableExtra(IntentExtra.EVENT)
        setToolbarTitle(getString(R.string.after_event_form_title, event.companyName))
        btn_submit.setOnClickListener { submitForm() }

        val previousFormData = intent.getParcelableExtra<CreatePostEventFormFields>(IntentExtra.POST_EVENT_FORM_FIELDS)
        previousFormData?.let {
            setupPreviousFormData(it)
        }
    }

    private fun setupPreviousFormData(form: CreatePostEventFormFields) {
        when (form.interestInRegularWork) {
            1 -> rg_interest_in_company.check(R.id.rb_interest_1)
            2 -> rg_interest_in_company.check(R.id.rb_interest_2)
            3 -> rg_interest_in_company.check(R.id.rb_interest_3)
            4 -> rg_interest_in_company.check(R.id.rb_interest_4)
            5 -> rg_interest_in_company.check(R.id.rb_interest_5)
        }
        rg_interest_in_company.children.forEach { it.isEnabled = false }

        et_motivate_interest.run {
            setText(form.interestInCompanyMotivation)
            isEnabled = false
        }

        et_improved.run {
            setText(form.eventImprovements)
            isEnabled = false
        }

        et_enjoyed.run {
            setText(form.eventFeedback)
            isEnabled = false
        }

        when (form.foodRating) {
            1 -> rg_food.check(R.id.rb_food_1)
            2 -> rg_food.check(R.id.rb_food_2)
            3 -> rg_food.check(R.id.rb_food_3)
            4 -> rg_food.check(R.id.rb_food_4)
            5 -> rg_food.check(R.id.rb_food_5)
        }
        rg_food.children.forEach { it.isEnabled = false }

        when (form.activitiesRating) {
            1 -> rg_food.check(R.id.rb_food_1)
            2 -> rg_food.check(R.id.rb_food_2)
            3 -> rg_food.check(R.id.rb_food_3)
            4 -> rg_food.check(R.id.rb_food_4)
            5 -> rg_food.check(R.id.rb_food_5)
        }
        rg_activities.children.forEach { it.isEnabled = false }

        when (form.atmosphereRating) {
            1 -> rg_atmosphere.check(R.id.rb_atmosphere_1)
            2 -> rg_atmosphere.check(R.id.rb_atmosphere_2)
            3 -> rg_atmosphere.check(R.id.rb_atmosphere_3)
            4 -> rg_atmosphere.check(R.id.rb_atmosphere_4)
            5 -> rg_atmosphere.check(R.id.rb_atmosphere_5)
        }
        rg_atmosphere.children.forEach { it.isEnabled = false }

        rg_qualified.check(
            if (form.qualifiedToWork) {
                R.id.rb_qualified_yes
            } else {
                R.id.rb_qualified_no
            }
        )
        rg_qualified.children.forEach { it.isEnabled = false }

        when (form.eventImpact) {
            EventImpact.POSITIVE -> rg_impact.check(R.id.rb_impact_positive)
            EventImpact.NEUTRAL -> rg_impact.check(R.id.rb_impact_neutral)
            EventImpact.NEGATIVE -> rg_impact.check(R.id.rb_impact_negative)
        }
        rg_impact.children.forEach { it.isEnabled = false }

        btn_submit.isEnabled = false
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

        val eventImpact = when (rg_impact.checkedRadioButtonId) {
            R.id.rb_impact_positive -> EventImpact.POSITIVE
            R.id.rb_impact_negative -> EventImpact.NEGATIVE
            R.id.rb_impact_neutral -> EventImpact.NEUTRAL
            else -> throw IllegalStateException()
        }

        val interestInRegularWork = when (rg_interest_in_company.checkedRadioButtonId) {
            R.id.rb_interest_1 -> 1
            R.id.rb_interest_2 -> 2
            R.id.rb_interest_3 -> 3
            R.id.rb_interest_4 -> 4
            R.id.rb_interest_5 -> 5
            else -> throw IllegalStateException()
        }

        val interestInCompanyMotivation = et_motivate_interest.text.toString()
        val eventImprovements = et_improved.text.toString()
        val eventFeedback = et_enjoyed.text.toString()

        val foodRating = when (rg_food.checkedRadioButtonId) {
            R.id.rb_food_1 -> 1
            R.id.rb_food_2 -> 2
            R.id.rb_food_3 -> 3
            R.id.rb_food_4 -> 4
            R.id.rb_food_5 -> 5
            else -> throw IllegalStateException()
        }

        val activitiesRating = when (rg_activities.checkedRadioButtonId) {
            R.id.rb_activities_1 -> 1
            R.id.rb_activities_2 -> 2
            R.id.rb_activities_3 -> 3
            R.id.rb_activities_4 -> 4
            R.id.rb_activities_5 -> 5
            else -> throw IllegalStateException()
        }

        val atmosphereRating = when (rg_atmosphere.checkedRadioButtonId) {
            R.id.rb_atmosphere_1 -> 1
            R.id.rb_atmosphere_2 -> 2
            R.id.rb_atmosphere_3 -> 3
            R.id.rb_atmosphere_4 -> 4
            R.id.rb_atmosphere_5 -> 5
            else -> throw IllegalStateException()
        }

        val qualifiedToWork = when (rg_qualified.checkedRadioButtonId) {
            R.id.rb_qualified_yes -> true
            R.id.rb_qualified_no -> false
            else -> throw IllegalStateException()
        }

        disposable?.dispose()
        disposable = studsRepository.postPostEventForm(
            event.id,
            interestInRegularWork,
            interestInCompanyMotivation,
            eventImprovements,
            eventFeedback,
            foodRating,
            activitiesRating,
            atmosphereRating,
            qualifiedToWork,
            eventImpact
        )
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                progress_bar.visibility = View.VISIBLE
                btn_submit.isEnabled = false
            }
            .doOnTerminate {
                progress_bar.visibility = View.GONE
                btn_submit.isEnabled = true
            }
            .subscribe {
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose().also {
            disposable = null
        }
    }

    companion object {
        fun makeIntent(context: Context, event: Event, postEventFormData: CreatePostEventFormFields?) =
            Intent(context, PostEventFormActivity::class.java).apply {
                putExtra(IntentExtra.EVENT, event)
                putExtra(IntentExtra.POST_EVENT_FORM_FIELDS, postEventFormData)
            }
    }
}
