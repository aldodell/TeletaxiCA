package com.psiqueylogos_ac.teletaxi_cliente

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.psiqueylogos_ac.teletaxi_lib.easyAddress

class RequestServiceActivity : AppCompatActivity() {
    val tag = "TLTC"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatLng: LatLng = LatLng(10.9, -66.0)
    private lateinit var btOrder: Button

    private var placeFields = listOf(
        Place.Field.NAME,
        Place.Field.LAT_LNG,
        Place.Field.ADDRESS,
        Place.Field.ADDRESS_COMPONENTS
    )

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_service)


        //Configure button order
        btOrder = findViewById(R.id.btOrder)
        btOrder.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.request_service)
                .setMessage(R.string.request_service_speech)
                .setPositiveButton(
                    R.string.confirm_request_service
                ) { _, _ ->
                    val mIntent = Intent(this, ConfirmServiceActivity::class.java)
                    startActivity(mIntent)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { d, _ -> d.dismiss() }
                .show()

        }

        //Check permission
        requestPermission(PERMISSION_ACCESS_FINE_LOCATION, this)

        //Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)

        }

        val clientPlaces = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragmentOrigin =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment_origin)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragmentOrigin.setPlaceFields(placeFields)
        autocompleteFragmentOrigin.setCountry("ve")
        autocompleteFragmentOrigin.setHint(getString(R.string.where_are_you))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentOrigin.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mapViewModel.originSelected.value = place.latLng
                order.origin = easyAddress(place)
            }

            override fun onError(status: Status) {
                Log.i(tag, "An error occurred: $status")

            }
        })


        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragmentDestination =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment_destination)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragmentDestination.setPlaceFields(placeFields)
        autocompleteFragmentDestination.setCountry("ve")
        autocompleteFragmentDestination.setHint(getString(R.string.where_are_you_going))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentDestination.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mapViewModel.destinationSelected.value = place.latLng
                order.destination = easyAddress(place)
                Log.i(tag, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(tag, "An error occurred: $status")
            }
        })

        //get last position
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            currentLatLng = LatLng(it.latitude, it.longitude)
            mapViewModel.position.value = currentLatLng

            val request =
                FindCurrentPlaceRequest
                    .newInstance(placeFields.minusElement(Place.Field.ADDRESS_COMPONENTS))

            clientPlaces.findCurrentPlace(request)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.placeLikelihoods.firstOrNull()?.let { p ->
                            autocompleteFragmentOrigin.setText(p.place.name)
                            order.origin = easyAddress(p.place)
                            order.originLatLng = currentLatLng
                            mapViewModel.originSelected.value = currentLatLng
                        }
                    }
                }
                .addOnFailureListener {
                    Log.wtf("findCurrentPlace", "onCreate: ${it.message}")
                }
        }
    }
}