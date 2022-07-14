package com.psiqueylogos_ac.teletaxi_cliente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.firebase.auth.FirebaseAuth
import com.psiqueylogos_ac.teletaxi_lib.Settings


class ConfirmServiceActivity : AppCompatActivity() {
    private lateinit var tvOrder: TextView
    private lateinit var btConfirmOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_service)
        //Get auth
        val auth = FirebaseAuth.getInstance()

        //Get settings
        val settings = Settings(this)

        //Views references
        tvOrder = findViewById(R.id.tvOrderDescription)
        btConfirmOrder = findViewById(R.id.btConfirmOrder)

        //Set up order text
        val orderText =
            "Servicio <b>desde:</b> ${order.originCaption}" +
                    "<br/><b>hasta:</b>${order.destinationCaption}<br/>" +
                    "<b>Distancia en km: <b/>${order.distance}<br/>" +
                    "<b>Costo referencial en $:</b> ${order.cost}"

        val spanned = HtmlCompat.fromHtml(orderText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvOrder.text = spanned

        //Setup confirm button
        btConfirmOrder.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(R.string.request_service_speech_2)
                .setPositiveButton(
                    R.string.confirm_request_service
                ) { _, _ ->

                    //setup order
                    order.customer.email = auth.currentUser!!.email!!
                    order.customer.phone = settings.phone
                    settings.currentOrder = order

                    val mIntent = Intent(this, StatusServiceActivity::class.java)
                    startActivity(mIntent)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { d, _ -> d.dismiss() }
                .show()

        }
    }
}