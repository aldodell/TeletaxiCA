package com.psiqueylogos_ac.teletaxi_cliente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psiqueylogos_ac.teletaxi_lib.Order

import com.psiqueylogos_ac.teletaxi_lib.Settings
import com.psiqueylogos_ac.teletaxi_lib.StatusOrder

class StatusServiceActivity : AppCompatActivity() {

    private lateinit var rbRequestSend: RadioButton
    private lateinit var rbRequestAccepted: RadioButton
    private lateinit var rbRequestArrived: RadioButton
    private lateinit var btGoBegin: Button
    private lateinit var settings: Settings
    private var idOrder = ""

    //Get Firestore reference
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_service)
        //Get views references
        rbRequestSend = findViewById(R.id.rbRequestSend)
        rbRequestAccepted = findViewById(R.id.rbRequestAccepted)
        rbRequestArrived = findViewById(R.id.rbArrived)
        btGoBegin = findViewById(R.id.btGoBegin)

        //get settings
        settings = Settings(this)

        //Check if id is not empty
        if (order.id.isNotEmpty()) {
            //Check if this order exits on database
            db.collection("orders")
                .document(order.id)
                .get()
                .addOnCompleteListener {
                    if (!it.result.exists()) {
                        addOrder()
                    }
                }

        } else {
            addOrder()
        }


        btGoBegin.setOnClickListener {
            order = Order()
            settings.currentOrder = null
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

    }

    private fun addOrder() {
        //If order does not exits on database add it:
        db.collection("orders")
            .add(order.map)
            .addOnCompleteListener { it1 ->
                if (it1.isSuccessful) {
                    rbRequestSend.isChecked = true
                    idOrder = it1.result.id
                    settings.idOrder = it1.result.id
                    settings.currentOrder = order

                    //Capture changes on order database object
                    //for change user interface
                    db.collection("orders")
                        .addSnapshotListener { value, _ ->
                            value?.let {
                                it.documentChanges.forEach { doc ->
                                    if (doc.document.id == idOrder) {
                                        order.from(
                                            doc.document.data,
                                            doc.document.id
                                        )
                                        if (order.status == StatusOrder.accepted.name) {
                                            rbRequestAccepted.isChecked = true
                                        }
                                        if (order.status == StatusOrder.arrived.name) {
                                            rbRequestArrived.isChecked = true
                                        }
                                    }
                                }
                            }
                        }
                }
            }
    }
}
