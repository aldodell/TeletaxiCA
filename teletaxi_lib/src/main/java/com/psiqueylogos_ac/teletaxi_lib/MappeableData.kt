package com.psiqueylogos_ac.teletaxi_lib


import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties


/**
 * Base class which transforms its properties on a mutable map
 * Mark field with Excluding annotation to avoid implement this field on transformation work.
 */
open class MappeableData {
    /**
     * Annotation for exclude a field from mapping
     */
    annotation class Excluding

    //val sdf = SimpleDateFormat("dd-MM-yyyy")
    private var dateFormat = "dd-MM-yyyy"

    /**
     * Transforms the MappeableData child class into a Map<String,Any> object.
     */
    fun toMap(): Map<String, Any> {
        var map = mutableMapOf<String, Any>()
        this::class.declaredMemberProperties.forEach {
            val p = it as KProperty1<MappeableData, Any>
            if (!p.annotations.contains(Excluding())) {
                map.put(it.name, p.get(this))
            }
        }
        return map
    }


    open fun from(map: Map<String, Any>, id: String): MappeableData {

        map.keys.forEach { key ->
            this::class.declaredMemberProperties.forEach { it ->
                if (it is KMutableProperty1) {
                    val prop = it as KMutableProperty1<MappeableData, Any>
                    if (!prop.annotations.contains(Excluding())) {
                        if (prop.name == key) {
                            prop.set(this, map[key]!!)
                        }
                    }
                }
            }
        }
        return this
    }


    fun toJson(): String? {
        val map = toMap()
        val json = JSONObject.wrap(map)?.toString()
        return json
    }

    fun toCSV(): String {
        val sdf = SimpleDateFormat(dateFormat)
        val map = toMap()
        var r = StringBuilder()
        map.keys.forEach { key ->

            var t = map[key]
            var z = ""

            if (t is Date) {
                z = sdf.format(t)
            } else {
                z = t as String
            }
            z = z.replace("\"", "\\\"")
            r.append("\"")
            r.append(z)
            r.append("\"")
            r.append(",")
        }
        r.removeSuffix(",")
        return r.toString()
    }

    fun fromJson(json: String) {
        val obj = JSONObject(json)

        this::class.declaredMemberProperties  .forEach {
            if(it is KMutableProperty1) {
                var prop = it as KMutableProperty1<MappeableData, Any>
                var key = it.name
                if (!prop.annotations.contains(Excluding())) {
                    prop.set(this, obj[key])
                }
            }
        }

    }

}