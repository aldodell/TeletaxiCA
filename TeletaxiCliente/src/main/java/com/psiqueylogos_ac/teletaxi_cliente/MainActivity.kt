package com.psiqueylogos_ac.teletaxi_cliente

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging

import com.psiqueylogos_ac.teletaxi_lib.Settings


/*
ORDEN DE ACTIVIDADES
MAIN -> RequestServiceActivity -> ConfirmServiveActivity -> StatusServiceActivity
 */


val mapViewModel = MapViewModel()
const val PERMISSION_ACCESS_COARSE_LOCATION = 1
const val PERMISSION_ACCESS_FINE_LOCATION = 1
var order = com.psiqueylogos_ac.teletaxi_lib.Order()


fun requestPermission(permission: Int, appCompatActivity: AppCompatActivity) {
    when (permission) {
        1 -> {
            ActivityCompat.requestPermissions(
                appCompatActivity,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_ACCESS_COARSE_LOCATION
            )
        }

        2 -> {
            ActivityCompat.requestPermissions(
                appCompatActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ACCESS_FINE_LOCATION
            )
        }
    }
}

fun requestService(context: Context) {
    requestPermission(PERMISSION_ACCESS_COARSE_LOCATION, context as AppCompatActivity)
    val mIntent = Intent(context, RequestServiceActivity::class.java)
    context.startActivity(mIntent)
}

fun createUser(context: Context) {
    val mIntent = Intent(context, GetEmailActivity::class.java)
    context.startActivity(mIntent)
}

class MainActivity : AppCompatActivity() {

    private lateinit var ibRequestService: ImageButton
    private lateinit var user: FirebaseUser
    private lateinit var tvAppVersion: TextView
    private lateinit var btManageAccount: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ibRequestService = findViewById(R.id.ibRequestService)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        btManageAccount = findViewById(R.id.btManageAccount)

        ibRequestService.isEnabled = false

        //Get app version
        val appVersion = BuildConfig.VERSION_CODE
        tvAppVersion.text = "Versión: $appVersion"

        FirebaseApp.initializeApp(this)
        val settings = Settings(this)

        if (FirebaseAuth.getInstance().currentUser == null) {
            if (settings.containsEmail) {
                val email = settings.email
                val password = settings.password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            user = it.result.user!!
                        }
                    }
            } else {
                createUser(this)
            }
        } else {
            ibRequestService.isEnabled = true
        }


        //Get messaging token
        if (settings.messageToken.isNullOrEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    settings.messageToken = it.result
                }
            }
        }

        //Setup button action
        ibRequestService.setOnClickListener {
            requestService(this)
        }

        //Manage account button
        btManageAccount.setOnClickListener {
            createUser(this)
        }


    }


}