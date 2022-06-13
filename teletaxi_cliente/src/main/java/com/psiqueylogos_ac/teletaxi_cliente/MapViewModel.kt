package com.psiqueylogos_ac.teletaxi_cliente

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
    var position = MutableLiveData<LatLng>()
    var destinationSelected = MutableLiveData<LatLng>()
    var originSelected = MutableLiveData<LatLng>()

}