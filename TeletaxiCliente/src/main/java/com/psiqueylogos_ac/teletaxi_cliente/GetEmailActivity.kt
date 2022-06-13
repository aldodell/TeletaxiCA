package com.psiqueylogos_ac.teletaxi_cliente

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.psiqueylogos_ac.teletaxi_lib.Settings

class GetEmailActivity : AppCompatActivity() {

    private lateinit var etMail: EditText
    private lateinit var btSendEmail: Button
    private lateinit var etPassword: EditText
    private lateinit var btPasswordRecovery: Button
    private lateinit var btCloseSession: Button
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_email)

        etMail = findViewById(R.id.etEmail)
        btSendEmail = findViewById(R.id.btSendEmail)
        etPassword = findViewById(R.id.etPassword)
        btPasswordRecovery = findViewById(R.id.btPasswordRecovery)
        btCloseSession = findViewById(R.id.btCloseSession)

        val settings = Settings(this)


        //load email from settings
        if (etMail.text.isBlank()) {
            if (settings.containsEmail) {
                etMail.setText(settings.email)
                etPassword.setText(settings.password)
            }
        }


        //Send email / password. If user exists sigin it, or create user.
        btSendEmail.setOnClickListener {
            val email = etMail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            settings.email = email
            settings.password = password

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { it1 ->
                    if (!it1.isSuccessful) {

                        //If does not exists user
                        if (it1.exception is FirebaseAuthInvalidUserException) {

                            //Create new user
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                    if (!it.isSuccessful) {
                                        if (it.exception is FirebaseAuthUserCollisionException) {
                                            Toast.makeText(
                                                this,
                                                "El usuario $email ya se encuentra registrado. Intenta con otro",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${it1.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        //If user exists and is valid say WElcome!!
                        Toast.makeText(
                            this,
                            "Bienvenido ${auth.currentUser?.email}",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
        }


        //Recovery account. Reset password
        btPasswordRecovery.setOnClickListener {
            val email = etMail.text.toString().trim()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se ha enviado un correo a $email para recuperar la contraseña.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.wtf("xx", "onCreate: ${it.exception.toString()}")
                        Toast.makeText(
                            this,
                            "Error: ${it.exception!!.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

        }

        btCloseSession.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            settings.email = ""
            settings.password = ""


        }

    }
}