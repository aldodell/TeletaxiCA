package com.psiqueylogos_ac.teletaxi_lib

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

interface DataMappeable {
    annotation class Excluding
    annotation class Mappeable

    var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            this::class.declaredMemberProperties.forEach {
                if (!it.hasAnnotation<Excluding>()) {
                    val p0 = it as KProperty1<DataMappeable, Any>
                    val obj = p0.get(this)
                    if (obj is DataMappeable) {
                        val obj2 = obj.map
                        r.put(p0.name, obj2)
                    } else {
                        r.put(p0.name, obj)
                    }

                }
            }
            return r
        }
        set(value) {
            this::class.declaredMemberProperties.forEach {
                if (!it.hasAnnotation<Excluding>()) {
                    if (it is KMutableProperty1) {
                        val p0 = it as KMutableProperty1<DataMappeable, Any>
                        if (value.contains(p0.name)) {
                            var obj = value[p0.name]!!
                            if (p0 is DataMappeable) {
                                obj = (obj as DataMappeable).map
                            }
                            p0.set(this, obj)
                        }
                    }
                }
            }

        }


}