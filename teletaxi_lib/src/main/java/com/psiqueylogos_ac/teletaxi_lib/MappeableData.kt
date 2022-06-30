package com.psiqueylogos_ac.teletaxi_lib


import org.json.JSONObject
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation


/**
 * Interface use to assure the class accomplish with map and json properties
 */
interface MappeableData {
    var map : MutableMap<String, Any>
    var json : JSONObject
}
