package com.studieresan.studs.happenings

import HappeningCreateMutation
import UsersQuery
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputLayout
import com.studieresan.studs.BuildConfig.PLACES_API_KEY
import com.studieresan.studs.R
import com.studieresan.studs.StudsApplication
import com.studieresan.studs.data.StudsPreferences
import com.studieresan.studs.graphql.apolloClient
import com.studieresan.studs.net.StudsRepository
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import type.GeoJSONFeatureInputType
import type.GeometryInputType
import type.HappeningInput
import type.PropertiesInputType
import javax.inject.Inject


class CreateHappeningActivity : AppCompatActivity() {

    private var selectedEmoji: RadioButton? = null
    private var disposable: Disposable? = null
    private var coordinates = arrayOf(18.074242F, 59.314797F)

    private var participants = listOf("5fb278e69fca82b6f5faf80e", "5fb278e69fca82b6f5faf806")

    @Inject
    lateinit var studsRepository: StudsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_happening)

        StudsApplication.applicationComponent.inject(this)


        // Initialize the SDK
        Places.initialize(applicationContext, PLACES_API_KEY)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                println("Place: ${place.name}, ${place.id}")
            }


            override fun onError(p0: Status) {
                // TODO: Handle the error.

                println("An error occurred: $p0")
            }
        })





        selectedEmoji = findViewById<View>(R.id.create_emoji1) as RadioButton

        val radioGroup = findViewById<View>(R.id.create_rb) as RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            selectedEmoji!!.alpha = 0.5F
            selectedEmoji = findViewById<View>(checkedId) as RadioButton
            selectedEmoji!!.alpha = 1F
        }


        // I think this should be done in a presenter :)
        val addButton = findViewById<View>(R.id.btn_add_happening) as Button
        addButton.setOnClickListener {
            createHappening(this)
        }

        loadUsers(this)
    }


    private fun loadUsers(context: Context) {

        CoroutineScope(Dispatchers.Main).launch {

            val response = try {
                apolloClient(context).query(UsersQuery()).await()
            } catch (e: ApolloException) {
                null
            }

            val users = response?.data?.users?.filterNotNull()

            val adapter = ArrayAdapter(context,
                    android.R.layout.simple_dropdown_item_1line, users?.map { user ->  "${user.firstName} ${user.lastName?.get(0)}"
                    } ?: listOf(""))
            val textView = findViewById<MultiAutoCompleteTextView>(R.id.create_participants)
            textView.setAdapter(adapter)
            textView.setTokenizer(CommaTokenizer())

        }

    }

    private fun createHappening(context: Context) {
        val description  = findViewById<View>(R.id.create_desc) as TextInputLayout
        val participants  = findViewById<View>(R.id.tv_create_participants)

        val happening = HappeningInput(
                host = StudsPreferences.getID(context).toInput(),
                participants = listOf("5fb278e69fca82b6f5faf80e", "5fb278e69fca82b6f5faf806").toInput(),
                location = GeoJSONFeatureInputType(type = "Feature".toInput(),
                        geometry = GeometryInputType(
                                type = "Point".toInput(),
                                coordinates = listOf(18.030078, 59.344294).toInput()).toInput(),
                        properties = PropertiesInputType("Idun".toInput()).toInput()).toInput(),
                title = "Coding & crying pt 2".toInput(),
                emoji = selectedEmoji?.text.toString().toInput(),
                description = description.editText?.text.toString().toInput())

        CoroutineScope(Dispatchers.Main).launch {
            val response = try {
                apolloClient(context).mutate(HappeningCreateMutation(happening = happening.toInput())).await()
            } catch (e: ApolloException) {
                null
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose().also {
            disposable = null
        }


        /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    */
    }

}