package com.psiqueylogos_ac.teletaxi_conductor

import org.json.JSONObject

class Driver {

    var userId = ""
    var messageToken = ""

    var map: MutableMap<String, Any>
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

    var json: JSONObject
        get() {
            val j = JSONObject(this.map as Map<*, *>?)
            return j
        }
        set(value) {
            userId = value["userId"] as String
            messageToken = value["messageToken"] as String
        }

}