package com.psiqueylogos_ac.teletaxica

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
    var position = MutableLiveData<LatLng>()
    var destinationSelected = MutableLiveData<LatLng>()
    var originSelected = MutableLiveData<LatLng>()

}