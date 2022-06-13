package com.psiqueylogos_ac.teletaxi_cliente

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.psiqueylogos_ac.teletaxi_lib.Order

import com.psiqueylogos_ac.teletaxi_lib.Settings


/*
ORDEN DE ACTIVIDADES
MAIN -> RequestServiceActivity -> ConfirmServiveActivity -> StatusServiceActivity
 */


val mapViewModel = MapViewModel()
const val PERMISSION_ACCESS_COARSE_LOCATION = 1
const val PERMISSION_ACCESS_FINE_LOCATION = 2
var order = Order()

/**
 * Request permission to get GPS signal and other stuff
 */
fun requestPermission(permission: Int, appCompatActivity: AppCompatActivity) {
    when (permission) {
        1 -> {
            if (ContextCompat.checkSelfPermission(
                    appCompatActivity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    appCompatActivity,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_ACCESS_COARSE_LOCATION
                )
            }
        }

        2 -> {
            if (ContextCompat.checkSelfPermission(
                    appCompatActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    appCompatActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_ACCESS_FINE_LOCATION
                )
            }
        }
    }
}

/**
 * Use toload RequestServiceActivity
 */
fun requestService(context: Context) {
    requestPermission(PERMISSION_ACCESS_COARSE_LOCATION, context as AppCompatActivity)
    val mIntent = Intent(context, RequestServiceActivity::class.java)
    context.startActivity(mIntent)
}

/**
 * Used to load GetEmailActivity
 */
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

        //Views references
        ibRequestService = findViewById(R.id.ibRequestService)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        btManageAccount = findViewById(R.id.btManageAccount)

//Views Configurations
        ibRequestService.isEnabled = false

        //Get app version
        val appVersion = BuildConfig.VERSION_CODE
        tvAppVersion.text = "Versión: $appVersion"

        //Initialize Firebase
        FirebaseApp.initializeApp(this)

        //Initialize settings
        val settings = Settings(this)

        //Get credentials to login
        if (FirebaseAuth.getInstance().currentUser == null) {
            //If we have and email saved restore it.
            if (settings.containsEmail) {
                val email = settings.email
                val password = settings.password
                //Sign in with email and password saved.
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            user = it.result.user!!
                        }
                    }
            } else {
                //If we haven't email call GetEmailActivity
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