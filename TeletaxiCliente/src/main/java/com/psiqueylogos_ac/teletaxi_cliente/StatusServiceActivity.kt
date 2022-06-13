package com.psiqueylogos_ac.teletaxi_cliente

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.psiqueylogos_ac.teletaxi_lib.Settings
import com.psiqueylogos_ac.teletaxi_lib.StatusOrder

class StatusServiceActivity : AppCompatActivity() {

    private lateinit var rbRequestSend: RadioButton
    private lateinit var rbRequestAccepted: RadioButton
    private lateinit var rbRequestArrived: RadioButton
    private lateinit var settings: Settings
    private var idOrder = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_service)

        rbRequestSend = findViewById(R.id.rbRequestSend)
        rbRequestAccepted = findViewById(R.id.rbRequestAccepted)
        rbRequestArrived = findViewById(R.id.rbArrived)

        settings = Settings(this)

        val db = Firebase.firestore

        //Send a request
        db.collection("orders")
            .add(order.toMap())
            .addOnCompleteListener { it1 ->
                if (it1.isSuccessful) {
                    rbRequestSend.isChecked = true
                    idOrder = it1.result.id
                    settings.idOrder = it1.result.id


                    //Capture changes
                    db.collection("orders")
                        .addSnapshotListener { value, _ ->
                            value?.let {
                                it.documentChanges.forEach { doc ->
                                    if (doc.document.id == idOrder) {
                                        order.from(doc.document.data, doc.document.id)
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