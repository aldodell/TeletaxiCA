package com.psiqueylogos_ac.teletaxica

import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceTo(destination: LatLng): Double {


    /**
     * Haversine formula. Giving great-circle distances between two points on a sphere from their longitudes and latitudes.
     * It is a special case of a more general formula in spherical trigonometry, the law of haversines, relating the
     * sides and angles of spherical "triangles".
     *
     * https://rosettacode.org/wiki/Haversine_formula#Java
     *
     * @return Distance in kilometers
     */
    val earthRadiusKm = 6372.8;
    val dLat = Math.toRadians(destination.latitude - this.latitude);
    val dLon = Math.toRadians(destination.longitude - this.longitude);
    val originLat = Math.toRadians(this.latitude);
    val destinationLat = Math.toRadians(destination.latitude);

    val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(
        Math.sin(dLon / 2),
        2.toDouble()
    ) * Math.cos(originLat) * Math.cos(destinationLat);
    val c = 2 * Math.asin(Math.sqrt(a));
    return earthRadiusKm * c;


}