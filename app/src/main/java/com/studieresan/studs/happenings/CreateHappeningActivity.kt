package com.studieresan.studs.happenings

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.studieresan.studs.BuildConfig.MAPS_API_KEY
import com.studieresan.studs.R
import com.studieresan.studs.StudsApplication
import com.studieresan.studs.data.models.HappeningInput
import com.studieresan.studs.data.models.Location
import com.studieresan.studs.data.models.LocationGeometry
import com.studieresan.studs.data.models.LocationProperties
import com.studieresan.studs.net.StudsRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CreateHappeningActivity : AppCompatActivity() {

    private var selectedEmoji: RadioButton? = null
    private var disposable: Disposable? = null
    private var coordinates = arrayOf(18.074242F, 59.314797F)
    //private var coordinates = floatArrayOf(18.074242F, 59.314797F)
    private var prop: LocationProperties = LocationProperties("Idun")
    private var geometry = LocationGeometry("Point", coordinates)
    private var location = Location("Feature", geometry, prop)
    private var participants = listOf("5fb278e69fca82b6f5faf80e", "5fb278e69fca82b6f5faf806")
    //private var host: User = User("5fb278e69fca82b6f5faf806", "Fanny","Curtsson", )
    private var currentHappening: HappeningInput = HappeningInput("5fb278e69fca82b6f5faf80f", participants, location, "Coding & crying", "🥂", "Hihihi hänger med Kotten o har der NAJS")

/*

            {"id":"60636aa43dd47900170732f9",
                "title":"Bärsar på kvarnen",
                "emoji":"🍻",
                "created":"2021-03-30T18:15:00.871Z",
                "description":"Kom och häng med coola IT-gruppen",

                "location":{
                    "type":"Feature",
                    "geometry":{
                        "type":"Point",
                        "coordinates":[18.074242,59.314797]},
                    "properties":{"name":"Kvarnen"}},
                "host":{"id":"5fb278e69fca82b6f5faf806",
                    "firstName":"Glenn","lastName":"Olsson",
                    "info":{"picture":"https://studs21.s3.eu-north-1.amazonaws.com/profile_pictures/glenn.jpg"}},
                "participants":[
                {"id":"5fb278e69fca82b6f5faf80f","firstName":"Fanny","lastName":"Curtsson","info":{"picture":"https://studs21.s3.eu-north-1.amazonaws.com/profile_pictures/fanny.jpg"}},
                {"id":"5fb278e69fca82b6f5faf80e","firstName":"Fredrik","lastName":"Norlin","info":{"picture":"https://studs21.s3.eu-north-1.amazonaws.com/profile_pictures/fredrik-n.jpg"}}]}


 */

    @Inject
    lateinit var studsRepository: StudsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_happening)

        StudsApplication.applicationComponent.inject(this)



        // Initialize the SDK
        Places.initialize(applicationContext, MAPS_API_KEY)

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
            println("trying to create a happening")
            disposable?.dispose()
            disposable = studsRepository
                    .createHappening(currentHappening)
                    .subscribeOn(Schedulers.io())
                    //.observeOn(view.mainScheduler)
                    .subscribe({
                        println("Successful mutation")
                    }, {
                        println("Not successful :(")
                    })
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