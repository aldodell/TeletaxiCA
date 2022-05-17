package com.psiqueylogos_ac.teletaxica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.firebase.auth.FirebaseAuth

class ConfirmServiceActivity : AppCompatActivity() {
    lateinit var tvOrder: TextView
    lateinit var btConfirmOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_service)
        tvOrder = findViewById(R.id.tvOrderDescription)
        btConfirmOrder = findViewById(R.id.btConfirmOrder)
        val orderText =
            "Servicio <b>desde:</b> ${order.origin}" +
                    "<br /> <b>hasta</b>${order.destination}<br/>" +
                    "<b>Distancia en km: <b/>${order.distance}<br/>" +
                    "<b>Costo referencial en $: </b> ${order.cost}"

        val spanned = HtmlCompat.fromHtml(orderText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvOrder.setText(spanned)

        btConfirmOrder.setOnClickListener {
            var auth = FirebaseAuth.getInstance()
            order.customer = auth.currentUser!!.email!!
            val mIntent = Intent(this, StatusServiceActivity::class.java)
            startActivity(mIntent)
        }

    }
}