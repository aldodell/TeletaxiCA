package com.psiqueylogos_ac.teletaxi_lib

import android.content.Context
import org.json.JSONObject

class Settings(var context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val containsEmail: Boolean
        get() = sharedPreferences.contains("email")

    var email: String
        get() = sharedPreferences.getString("email", "")!!
        set(value) = sharedPreferences.edit().putString("email", value).apply()


    var phone: String
        get() = sharedPreferences.getString("phone", "")!!
        set(value) = sharedPreferences.edit().putString("phone", value).apply()

    var password: String
        get() = sharedPreferences.getString("password", "")!!
        set(value) = sharedPreferences.edit().putString("password", value).apply()

    var idOrder: String
        get() = sharedPreferences.getString("idOrder", "")!!
        set(value) = sharedPreferences.edit().putString("idOrder", value).apply()

    var messageToken: String?
        get() = sharedPreferences.getString("messageToken", "")
        set(value) = sharedPreferences.edit().putString("messageToken", value).apply()

    var currentOrder: Order?
        get() {
            var order = Order()
            val json = sharedPreferences.getString("currentOrder", "")
            return if (!json.isNullOrBlank()) {
                DataBox(order).json = JSONObject(json)
                //order.json = JSONObject(json)
                order
            } else {
                null
            }
        }
        set(value) {
            if (value != null) {
                //sharedPreferences.edit().putString("currentOrder", DataBox(value).json.toString())
                sharedPreferences.edit().putString("currentOrder", DataBox(value).json.toString())
                    .apply()
            } else sharedPreferences.edit().clear().apply()
        }

}