package com.psiqueylogos_ac.teletaxi_lib

import com.google.android.gms.maps.model.LatLng
import com.psiqueylogos_ac.datamap.DataMap

class MyLatLng(var latitude: Double, var longitude: Double) : DataMap {
    @DataMap.Excluding
    var latLng: LatLng
        get() = LatLng(latitude, longitude)
        set(value) {
            latitude = value.latitude
            longitude = value.longitude
        }
}