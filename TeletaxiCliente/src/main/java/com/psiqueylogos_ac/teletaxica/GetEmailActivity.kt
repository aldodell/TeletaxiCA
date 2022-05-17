package com.psiqueylogos_ac.teletaxica

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class GetEmailActivity : AppCompatActivity() {

    lateinit var etMail: EditText
    lateinit var btSendEmail: Button
    lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_email)

        etMail = findViewById(R.id.etEmail)
        btSendEmail = findViewById(R.id.btSendEmail)
        etPassword = findViewById(R.id.etPassword)

        btSendEmail.setOnClickListener {
            val email = etMail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val auth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener { it ->
                    if (it is FirebaseAuthInvalidUserException) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Error creando usuario: " + it.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .addOnCompleteListener { t1 ->
                                if (t1.isSuccessful) {
                                    auth.signInWithEmailAndPassword(email, password)
                                }
                            }
                            .addOnCompleteListener { t2 ->
                                if (t2.isSuccessful) {
                                    finish()
                                }
                            }
                    }
                }
        }
    }
}
