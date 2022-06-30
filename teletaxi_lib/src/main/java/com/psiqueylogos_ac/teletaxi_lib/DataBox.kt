package com.psiqueylogos_ac.teletaxi_lib

import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

class DataBox<T : Any>(var source: T) {

    annotation class Excluding

    val map: MutableMap<String, Any>
        get() {
            var r = mutableMapOf<String, Any>()
            source::class.declaredMemberProperties.forEach { p ->
                if (!p.hasAnnotation<Excluding>()) {
                    val p0 = p as KProperty1<T, Any>
                    r[p0.name] = p.get(source)
                }
            }
            return r
        }
}