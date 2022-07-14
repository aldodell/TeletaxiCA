package com.psiqueylogos_ac.teletaxi_lib

import com.psiqueylogos_ac.datamap.DataMap

class Customer : DataMap {
    /*: MappeableData()*/
    //dataSource: MutableMap<String, Any>? = null
    // dataSource
    var phone: String = ""
    var email: String = ""

    /*
    var map: MutableMap<String, Any>
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

    var json: JSONObject
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