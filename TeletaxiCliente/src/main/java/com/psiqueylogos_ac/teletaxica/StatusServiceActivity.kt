package com.psiqueylogos_ac.teletaxica

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StatusServiceActivity : AppCompatActivity() {

    lateinit var rbRequestSend: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_service)

        rbRequestSend = findViewById(R.id.rbRequestSend)

        //Send a request
        val db = Firebase.firestore
        db.collection("orders")
            .add(order.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    rbRequestSend.isChecked = true
                }
            }

    }


}