package com.psiqueylogos_ac.teletaxi_lib

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import kotlin.math.ceil
import kotlin.math.round


fun easyAddress(place: Place): String {
    val r = StringBuilder()
    val suffix = ", "
    if (place.name == null) {
        r.append(place.name)
        r.append(suffix)
    }

    place.addressComponents?.asList()?.forEach { c ->
        r.append(c.name)
        r.append(suffix)
    }
    r.removeSuffix(suffix)
    return r.toString()
}


enum class StatusOrder(status: Int) {
    pending(0),
    accepted(1),
    arrived(2)
}


class Order : MappeableData() {

    var customer = ""
    var driver = ""
    var origin = ""
    var destination = ""
    var originLat = 0.0
    var originLon = 0.0
    var destinationLat = 0.0
    var destinationLon = 0.0

    var status: String = StatusOrder.pending.name

    @Excluding
    var id = ""

    @Excluding
    var originLatLng: LatLng
        get() = LatLng(originLat, originLon)
        set(value) {
            originLat = value.latitude; originLon = value.longitude
        }

    @Excluding
    var destinationLatLng: LatLng
        get() = LatLng(destinationLat, destinationLon)
        set(value) {
            destinationLat = value.latitude; destinationLon = value.longitude
        }


    val distance: Double
        get() = ceil(originLatLng.distanceTo(destinationLatLng))

    /**
     * Return cost of service based on members criteria
     */
    val cost: Double
        get() {
            /*
                0-6 1,2
                7-20 1
                21-40 0,8
            */

            var d = distance
            var t = 0.0;

            if (d > 20) {
                t = (d - 20.0) * 0.8;
                d = 20.0;
            }

            if (d > 6) {
                t += ((d - 6) * 1.0);
                d = 6.0;
            }

            t += (d * 1.2);

            if (t < 5) t = 5.0;

            return round(t)
        }


    override fun from(map: Map<String, Any>, id: String): MappeableData {
        this.id = id
        return super.from(map, id)
    }

}