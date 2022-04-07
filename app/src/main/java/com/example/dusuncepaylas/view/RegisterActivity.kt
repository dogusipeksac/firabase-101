package com.example.dusuncepaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dusuncepaylas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
    }


    fun signUp(view: View) {
        val email = user_email_register.text.toString()
        val password = user_password.text.toString()
        val userName = user_name.text.toString()
        if (password != "" && email != "") {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val onlineUser = auth.currentUser
                        val profileUpdates = userProfileChangeRequest {
                            displayName = userName
                        }
                        /*
                        şifre değiştirme ve email değiştirme işlemini buradan yapabiliriz
                        onlineUser!!.updatePassword("")
                        onlineUser!!.updateEmail("")
                         */
                        onlineUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Profil kullanici adı eklendi!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            applicationContext,
                            "Başarıyla oluşturuldu",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}