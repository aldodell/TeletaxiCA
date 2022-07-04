package com.psiqueylogos_ac.teletaxi_conductor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.psiqueylogos_ac.teletaxi_lib.DataMappeable
import com.psiqueylogos_ac.teletaxi_lib.Settings


/**
 *
 */
fun showPendingServices(context: Context) {

    //Get messaging token
    FirebaseMessaging.getInstance()
        .token
        .addOnCompleteListener {
            if (it.isSuccessful) {
                sendToken(context, it.result)
            }
        }

    val mIntent = Intent(context, PendingServicesActivity::class.java)
    context.startActivity(mIntent)
}

fun showCurrentService(context: Context) {
    val mIntent = Intent(context, CurrentServiceActivity::class.java)
    context.startActivity(mIntent)
}

/**
 * Save current message token to database and app's settings
 */
fun sendToken(context: Context, token: String) {
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val settings = Settings(context)

    if (auth.currentUser != null) {

        val driver = Driver()
        driver.userId = auth.currentUser!!.uid
        driver.messageToken = token
        settings.messageToken = token

        db.collection("drivers_device")
            .whereEqualTo("messageToken", token)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.isEmpty) {
                        db.collection("drivers_device")
                            .add(driver.map)
                    }
                }
            }
    }

}


class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btEntry: Button
    private lateinit var btClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        data class Example(var name: String, var age: Int, var parent: Example? = null)

        val ex = Example("a", 22, Example("b", 5, Example("c", 8)))
        val ex2 = Example("", 0)
        val j = DataBox(ex).json
        DataBox(ex2).json = j

         */

        class class1 : DataMappeable {
            var a = 1
            var b = "2"
        }

        val c1 = class1()


        //Init firebase
        Firebase.initialize(this)

        //get authorization instance object
        val auth = FirebaseAuth.getInstance()

        //Get views references
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btEntry = findViewById(R.id.btEntry)
        btClose = findViewById(R.id.btClose)


        //set entry button action
        btEntry.setOnClickListener {
            val settings = Settings(this)
            settings.email = etEmail.text.toString().trim()
            settings.password = etPassword.text.toString().trim()
            processAccess()
        }

        //set close button action
        btClose.setOnClickListener {
            auth.signOut()
        }

        //itself
        processAccess()

        //Suscribe to a topic messaging
        FirebaseMessaging.getInstance().subscribeToTopic("services")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Suscrito al canal de notificaciones", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    /**
     * Implement logic to grant access through firebase auth credentials
     * creating a new user if doesn't exists.
     */
    private fun processAccess() {
        val auth = FirebaseAuth.getInstance()
        val settings = Settings(this)

        var email = ""
        var password = ""

        if (settings.containsEmail) {
            email = settings.email
            password = settings.password
        } else {
            email = etEmail.text.toString().trim()
            password = etEmail.text.toString().trim()
        }

        //do not Exists this user ???
        if (auth.currentUser == null) {
            if (email.isNotEmpty() && password.isNotEmpty())
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        //Successful with user recognized
                        if (it.isSuccessful) {
                            showPendingServices(this)
                        } else {
                            //Show error:
                            Toast.makeText(
                                this,
                                "Error login: ${it.exception!!.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        } else {
            if (settings.idOrder.isEmpty()) {
                showPendingServices(this)
            } else {
                showCurrentService(this)
            }
        }
    }

}