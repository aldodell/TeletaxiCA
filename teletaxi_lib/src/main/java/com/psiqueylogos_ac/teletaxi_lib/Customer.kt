package com.psiqueylogos_ac.teletaxi_lib

class Customer() /*: MappeableData()*/ {
//dataSource: MutableMap<String, Any>? = null
   // dataSource
    var phone: String = ""
    var email: String = ""
/*
    override var map: MutableMap<String, Any>
        get() {
            val r = mutableMapOf<String, Any>()
            r["phone"] = phone
            r["email"] = email
            return r
        }
        set(value) {
            phone = value["phone"] as String
            email = value["email"] as String
        }

    override var json: JSONObject
        get() {
            val j = JSONObject(this.map as Map<*, *>?)
            return j
        }
        set(value) {
            phone = value["phone"] as String
            email = value["email"] as String
        }
        */


}