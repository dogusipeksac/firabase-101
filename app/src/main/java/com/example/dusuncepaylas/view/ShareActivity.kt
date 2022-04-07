package com.example.dusuncepaylas.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dusuncepaylas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_share.*


class ShareActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        auth = Firebase.auth
    }

    fun shareButton(view: View) {
        val postDescribe = share_text.text.toString()
        val userName = auth.currentUser!!.displayName.toString()
        val date = com.google.firebase.Timestamp.now()

        val shareMap = hashMapOf<String, Any>()
        shareMap["postDescribe"] = postDescribe
        shareMap["userName"] = userName
        shareMap["date"] = date

        db.collection("Posts").add(shareMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Successful!", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, "Erorr:${exception}", Toast.LENGTH_SHORT)
        }
    }

}