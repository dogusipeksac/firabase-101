package com.example.dusuncepaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dusuncepaylas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.user_password

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }


    fun signIn(view: View) {
        val email = user_email_login.text.toString()
        val password = user_password.text.toString()
        if (password != "" && email != "") {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /*
                    şifremi unuttum gibi özellikler için
                    val emailAddress="user@example.com"
                    Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener {  }
                     */
                    val guncelKullanici = auth.currentUser?.displayName.toString()
                    Toast.makeText(
                        applicationContext,
                        "$guncelKullanici -> Hoşgeldin",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}