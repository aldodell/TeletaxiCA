package com.psiqueylogos_ac.teletaxi_lib

import android.content.Context

class Settings(var context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val containsEmail: Boolean
        get() = sharedPreferences.contains("email")

    var email: String
        get() = sharedPreferences.getString("email", "")!!
        set(value) = sharedPreferences.edit().putString("email", value).apply()

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
            order.fromJson(sharedPreferences.getString("currentOrder", "")!!)
            return order
        }
        set(value) = sharedPreferences.edit().putString("currentOrder", value!!.toJson()!!).apply()

}