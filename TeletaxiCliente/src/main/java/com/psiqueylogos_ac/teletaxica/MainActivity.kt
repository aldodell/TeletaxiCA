package com.psiqueylogos_ac.teletaxica

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


val mapViewModel = MapViewModel()
val PERMISSION_ACCESS_COARSE_LOCATION = 1
val PERMISSION_ACCESS_FINE_LOCATION = 1
var order = Order()
lateinit var sharedPreferences: SharedPreferences

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

class MainActivity : AppCompatActivity() {

    lateinit var btRequestService: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btRequestService = findViewById(R.id.btRequestService)

        FirebaseApp.initializeApp(this)

        //Get shared preferences
        sharedPreferences = this.getSharedPreferences("settings", AppCompatActivity.MODE_PRIVATE)

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val mIntent = Intent(this, GetEmailActivity::class.java)
            startActivity(mIntent)
        }

        btRequestService.setOnClickListener {
            val mIntent = Intent(this, RequestServiceActivity::class.java)
            startActivity(mIntent)
        }


    }
}