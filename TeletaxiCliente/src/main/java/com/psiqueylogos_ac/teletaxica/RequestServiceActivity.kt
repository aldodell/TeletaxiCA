package com.psiqueylogos_ac.teletaxica

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class RequestServiceActivity : AppCompatActivity() {
    val TAG = "TELETAXI"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatLng: LatLng = LatLng(10.9, -66.0)
    lateinit var btOrder: Button

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_service)


        //Configure button order
        btOrder = findViewById(R.id.btOrder)
        btOrder.setOnClickListener {
            var order = Order()

            val mIntent = Intent(this, ConfirmServiceActivity::class.java)
            startActivity(mIntent)
        }


        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(PERMISSION_ACCESS_FINE_LOCATION, this)
        }

        //get last position
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            currentLatLng = LatLng(it.latitude, it.longitude)
            mapViewModel.position.value = currentLatLng
        }


        //Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
            Places.createClient(this)

        }


        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragmentOrigin =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment_origin)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragmentOrigin.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragmentOrigin.setCountry("ve")
        autocompleteFragmentOrigin.setHint("¿Dónde estás?")


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentOrigin.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mapViewModel.originSelected.value = place.latLng
                order.origin = place.name as String


            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })


        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragmentDestination =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment_destination)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragmentDestination.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragmentDestination.setCountry("ve")
        autocompleteFragmentDestination.setHint("¿A dónde vas?")

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentDestination.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mapViewModel.destinationSelected.value = place.latLng
                order.destination = place.name as String
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })


    }

}