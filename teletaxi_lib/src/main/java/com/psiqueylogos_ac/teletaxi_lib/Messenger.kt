package com.psiqueylogos_ac.teletaxi_lib

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

val TAG = "TELETAXI"

fun sendFMC(text: String) {
    val messageId = "585448872153"
    val fm = FirebaseMessaging.getInstance()
    val rm = RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
        .setMessageId(messageId)
        .addData("my_message", text)
        .addData("my_action", "SAY_HELLO")
        .build()
    fm.send(rm)
}


fun sendMessage(
    context: Context,
    projectID: String,
    topic: String,
    text: String
) {

    Thread().run {

        val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
        val SCOPES = arrayOf(MESSAGING_SCOPE)
        var token = ""

        try {
            val credential =
                GoogleCredentials.fromStream(context.resources.openRawResource(R.raw.service_account))
                    .createScoped(SCOPES.toList())
            credential.refreshAccessToken()
            token = credential.accessToken.tokenValue

            val stringRequest = object : StringRequest(
                Method.POST,
                "https://fcm.googleapis.com/v1/projects/$projectID/messages:send",
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }) {

                override fun getBody(): ByteArray {
                    val data = mapOf("data" to JSONObject(text), "topic" to topic)
                    return JSONObject(data).toString().toByteArray()

                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    return mutableMapOf("authorization" to "Bearer $token")
                }
            }
            Volley.newRequestQueue(context).add(stringRequest)
        } catch (ex: Exception) {
             Log.wtf(TAG, "sendMessage: ${ex.message}")
        }


    }


}
/*
    val stringRequest = StringRequest(
        Request.Method.POST,
        "https://fcm.googleapis.com/v1/projects/$projectID/messages:send",
        {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        },
        {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    )

 */

