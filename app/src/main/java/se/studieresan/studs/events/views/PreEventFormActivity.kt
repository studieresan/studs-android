package se.studieresan.studs.events.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pre_event_form.*
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.StudsApplication
import se.studieresan.studs.data.IntentExtra
import se.studieresan.studs.data.models.CompanyFamiliarity
import se.studieresan.studs.data.models.CreatePreEventFormFields
import se.studieresan.studs.data.models.Event
import se.studieresan.studs.net.StudsRepository
import javax.inject.Inject

class PreEventFormActivity : StudsActivity() {

    @Inject
    lateinit var studsRepository: StudsRepository

    private lateinit var event: Event

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StudsApplication.applicationComponent.inject(this)
        setContentView(R.layout.activity_pre_event_form)
        addToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        event = intent.getParcelableExtra(IntentExtra.EVENT)
        setToolbarTitle(getString(R.string.pre_event_form_title, event.companyName))
        btn_submit.setOnClickListener { submitForm() }

        val previousFormData = intent.getParcelableExtra<CreatePreEventFormFields>(IntentExtra.PRE_EVENT_FORM_FIELDS)
        previousFormData?.let {
            setupPreviousFormData(it)
        }
    }

    private fun setupPreviousFormData(form: CreatePreEventFormFields) {
        when (form.familiarWithCompany) {
            CompanyFamiliarity.YES -> rg_familiarity.check(R.id.rb_familiarity_yes)
            CompanyFamiliarity.SOMEWHAT -> rg_familiarity.check(R.id.rb_familiarity_somewhat)
            CompanyFamiliarity.NO -> rg_familiarity.check(R.id.rb_familiarity_no)
        }
        rg_familiarity.children.forEach { it.isEnabled = false }

        when (form.interestInRegularWorkBefore) {
            1 -> rg_interest_in_company.check(R.id.rb_interest_1)
            2 -> rg_interest_in_company.check(R.id.rb_interest_2)
            3 -> rg_interest_in_company.check(R.id.rb_interest_3)
            4 -> rg_interest_in_company.check(R.id.rb_interest_4)
            5 -> rg_interest_in_company.check(R.id.rb_interest_5)
        }
        rg_interest_in_company.children.forEach { it.isEnabled = false }

        et_motivate_interest.run {
            setText(form.interestInCompanyMotivationBefore)
            isEnabled = false
        }

        et_describe_view.run {
            setText(form.viewOfCompany)
            isEnabled = false
        }

        btn_submit.isEnabled = false
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

        val familiarity = when (rg_familiarity.checkedRadioButtonId) {
            R.id.rb_familiarity_yes -> CompanyFamiliarity.YES
            R.id.rb_familiarity_somewhat -> CompanyFamiliarity.SOMEWHAT
            R.id.rb_familiarity_no -> CompanyFamiliarity.NO
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

        val viewOfCompany = et_describe_view.text.toString()
        val interestInCompanyMotivation = et_motivate_interest.text.toString()

        disposable?.dispose()
        disposable = studsRepository.postPreEventForm(
            event.id,
            interestInRegularWork,
            viewOfCompany,
            interestInCompanyMotivation,
            familiarity
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        fun makeIntent(context: Context, event: Event, preEventFormData: CreatePreEventFormFields?) =
            Intent(context, PreEventFormActivity::class.java).apply {
                putExtra(IntentExtra.EVENT, event)
                putExtra(IntentExtra.PRE_EVENT_FORM_FIELDS, preEventFormData)
            }
    }
}
