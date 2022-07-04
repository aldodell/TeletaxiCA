package com.psiqueylogos_ac.teletaxi_lib

import org.json.JSONObject
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation


class DataBox<T : Any>(var source: T) {

    annotation class Excluding
    annotation class DataBoxable

    var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    val p0 = p as KProperty1<T, Any>
                    r[p0.name] = p0.get(source)
                }
            }
            return r
        }
        set(value) {
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    if (p is KMutableProperty1) {
                        val p0 = p as KMutableProperty1<T, Any>
                        if (value.contains(p0.name)) {
                            val obj = value[p.name]
                            if (obj != null) {
                                if (p0.hasAnnotation<DataBoxable>()) {
                                    p0.setValue(source, p0, obj)
                                } else {
                                    p0.set(source, obj)
                                }
                            }
                        }
                    }
                }
            }
        }

    var json: JSONObject
        get() {
            val result = JSONObject()
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    val p0 = p as KProperty1<T, Any>
                    if (p0.hasAnnotation<DataBoxable>()) {
                        val obj = JSONObject.wrap(p0.get(source))
                        result.put(p.name, obj)
                    } else {
                        result.put(p.name, p0.get(source))
                    }
                }
            }
            return result
        }
        set(value) {
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    if (p is KMutableProperty1) {
                        val p0 = p as KMutableProperty1<T, Any>
                        if (value.has(p0.name)) {
                            val obj = value[p.name]
                            if (obj != null) {
                                p0.set(source, obj)
                            }
                        }
                    }
                }
            }
        }

}


/*
import org.json.JSONObject
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

class DataBox<T : Any>(var source: T) {

    annotation class Excluding
    annotation class DataBoxable

    var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    val p0 = p as KProperty1<T, Any>
                    r[p0.name] = p.get(source)
                }
            }
            return r
        }
        set(value) {
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    if (p is KMutableProperty1) {
                        val p0 = p as KMutableProperty1<T, Any>
                        if (value.contains(p0.name)) {
                            val obj = value[p.name]
                            if (obj != null) {
                                p0.set(source, obj)
                            }
                        }
                    }
                }
            }
        }

    var json: JSONObject
        get() {
            return JSONObject((this.map as Map<*, *>?)!!)
        }
        set(value) {
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    if (p is KMutableProperty1) {
                        val p0 = p as KMutableProperty1<T, Any>
                        if (!value.isNull(p0.name)) {
                            val obj = value[p.name]
                            p0.set(source, obj)
                        }
                    }
                }
            }
        }
}
*/