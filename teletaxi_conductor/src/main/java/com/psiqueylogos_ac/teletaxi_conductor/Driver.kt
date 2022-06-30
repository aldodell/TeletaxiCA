package com.psiqueylogos_ac.teletaxi_conductor

import com.psiqueylogos_ac.teletaxi_lib.MappeableData
import org.json.JSONObject

class Driver : MappeableData {
    // @Excluding
    var userId = ""
    var messageToken = ""


    override var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            r["userId"] = userId
            r["messageToken"] = messageToken
            return r
        }
        set(value) {
            userId = value["userId"] as String
            messageToken = value["messageToken"] as String
        }

    override var json: JSONObject
        get() {
            val j = JSONObject(this.map as Map<*, *>?)
            return j
        }
        set(value) {
            userId = value["userId"] as String
            messageToken = value["messageToken"] as String
        }

}