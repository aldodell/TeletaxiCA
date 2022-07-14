package com.psiqueylogos_ac.teletaxi_lib

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.psiqueylogos_ac.datamap.DataMap
import kotlin.math.ceil
import kotlin.math.round


fun easyAddress(place: Place): String {
    val r = StringBuilder()
    val suffix = ", "

    if (place.name != null) {
        r.append(place.name)
        r.append(suffix)
    }

    if (place.address != null) {
        r.append(place.address)
        r.append(suffix)
    }

    if (place.addressComponents != null) {
        place.addressComponents.asList().forEach { addressComponent ->
            if (
                addressComponent.types.contains("administrative_area_level_1") ||
                addressComponent.types.contains("administrative_area_level_2") ||
                addressComponent.types.contains("administrative_area_level_3") ||
                addressComponent.types.contains("street_address") ||
                addressComponent.types.contains("street_number") ||
                addressComponent.types.contains("locality") ||
                addressComponent.types.contains("sublocality") ||
                addressComponent.types.contains("neighborhood") ||
                addressComponent.types.contains("establishment") ||
                addressComponent.types.contains("political")

            ) {
                r.append(addressComponent.name)
                r.append(suffix)
            }

        }
    }

    r.removeSuffix(suffix)
    return r.toString()

}


fun easyAddress(geocoder: Geocoder, latLng: LatLng): String {
    val addrs = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    val r = StringBuilder()
    val ad = addrs.first()
    val suffix = ","
    r.append(ad.featureName)
    r.append(suffix)
    r.append(ad.adminArea)
    r.append(suffix)
    r.append(ad.subAdminArea)
    r.append(suffix)
    r.append(ad.locality)
    r.append(suffix)
    r.append(ad.subLocality)
    r.append(suffix)
    r.removeSuffix(suffix)
    return r.toString()

}


enum class StatusOrder(status: Int) {
    pending(0),
    accepted(1),
    arrived(2)
}




class Order : DataMap {

    /*
    var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            r["driver"] = driver
            r["origin"] = origin
            r["destination"] = destination
            r["originLat"] = originLat
            r["originLon"] = originLon
            r["destinationLat"] = destinationLat
            r["destinationLon"] = destinationLon
            r["status"] = status
            r["customer"] = customer
            return r

        }
        set(value) {
            driver = value["driver"] as String
            origin = value["origin"] as String
            destination = value["destination"] as String
            originLat = value["originLat"] as Double
            originLon = value["originLon"] as Double
            destinationLat = value["destinationLat"] as Double
            destinationLon = value["destinationLon"] as Double
            status = value["status"] as String

            val c = value["customer"] as HashMap<String, Any>
            val customer0 = Customer()
            customer0.email = c["email"] as String
            customer0.phone = c["phone"] as String

            customer = customer0
        }

    var json: JSONObject
        get() {
            val j = JSONObject(this.map as Map<*, *>?)
            return j
        }
        set(value) {

            driver = value["driver"] as String
            origin = value["driver"] as String
            destination = value["driver"] as String
            originLat = value["driver"] as Double
            originLon = value["driver"] as Double
            destinationLat = value["driver"] as Double
            destinationLon = value["driver"] as Double
            status = value["driver"] as String

            val c = value["customer"] as JSONObject
            val customer0 = Customer()
            customer0.email = c["email"] as String
            customer0.phone = c["phone"] as String
            customer = customer0
        }

*/

    //@Mappeable
//  @DataBox.DataBoxable
    var customer = Customer()

    var position = MyLatLng(0.0,0.0)

    var driver = ""
    var origin = ""
    var destination = ""
    var originLat = 0.0
    var originLon = 0.0
    var destinationLat = 0.0
    var destinationLon = 0.0
    var status: String = StatusOrder.pending.name

    @DataMap.Excluding
    var id = ""

    @DataMap.Excluding
    var originLatLng: LatLng
        get() = LatLng(originLat, originLon)
        set(value) {
            originLat = value.latitude; originLon = value.longitude
        }

    @DataMap.Excluding
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


    fun from(themap: Map<String, Any>, id: String) {
        this.map = themap as MutableMap<String, Any>
        this.id = id
    }


}