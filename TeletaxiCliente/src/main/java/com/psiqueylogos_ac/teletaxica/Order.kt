package com.psiqueylogos_ac.teletaxica

import com.google.android.gms.maps.model.LatLng
import kotlin.math.ceil
import kotlin.math.round

class Order : MappeableData() {
    var customer = ""
    var driver = ""
    var origin = ""
    var destination = ""
    var originLatLng = LatLng(0.0, 0.0)
    var destinationLatLng = LatLng(0.0, 0.0)
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


}