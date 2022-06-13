package com.psiqueylogos_ac.teletaxi_conductor

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



import com.psiqueylogos_ac.teletaxi_lib.Settings
import com.psiqueylogos_ac.teletaxi_lib.StatusOrder

class CurrentServiceActivity : AppCompatActivity() {

    private lateinit var tvServiceDescription: TextView
    private lateinit var btNotifyArrived: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_service)

        tvServiceDescription = findViewById(R.id.tvServiceDescription2)
        btNotifyArrived = findViewById(R.id.btNotifyArrived)

        //Load current service
        val db = Firebase.firestore
        val settings = Settings(this)
        db.collection("orders")
            .document(settings.idOrder)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val orderText =
                        "Servicio al usuario <b>" + it.result["customer"] + "</b> desde <b>" + it.result["origin"] + "</b> hasta <b>" + it.result["destination"] + "</b> costo <b>" + it.result["cost"] + "</b>"
                    val spanned = HtmlCompat.fromHtml(orderText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    tvServiceDescription.text = spanned
                }
            }

        btNotifyArrived.setOnClickListener {
            db.collection("orders")
                .document(settings.idOrder)
                .update("status", StatusOrder.arrived)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        settings.idOrder = ""
                        showPendingServices(this)
                    }
                }
        }
    }
}