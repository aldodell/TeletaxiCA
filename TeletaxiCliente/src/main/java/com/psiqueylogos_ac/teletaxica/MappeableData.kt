package com.psiqueylogos_ac.teletaxica


import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties


/**
 * Base class wich transforms its propeties on a mutable map
 */
open class MappeableData {
    //val sdf = SimpleDateFormat("dd-MM-yyyy")

    private var dateFormat = "dd-MM-yyyy"


    fun toMap(): Map<String, Any> {
        var map = mutableMapOf<String, Any>()
        this::class.declaredMemberProperties.forEach {
            val p = it as KProperty1<MappeableData, Any>
            map.put(it.name, p.get(this))
        }
        return map
    }

    fun from(map: Map<String, Any>) {
        map.keys.forEach { key ->
            this::class.declaredMemberProperties.forEach { it ->
                val prop = it as KMutableProperty1<MappeableData, Any>
                if (prop.name == key) {
                    prop.set(this, map[key]!!)
                }
            }
        }
    }


    fun toJson(): String {
        val map = toMap()
        val json = JSONObject.wrap(map).toString()
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

}