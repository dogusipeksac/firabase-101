package com.example.dusuncepaylas.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dusuncepaylas.MainActivity
import com.example.dusuncepaylas.R
import com.example.dusuncepaylas.adapter.PostAdapter
import com.example.dusuncepaylas.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    var postList = ArrayList<Post>()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.ana_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cikisyap) {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        getFirebaseData()
        val layoutManager = LinearLayoutManager(this)
        postRecyclerView.layoutManager = layoutManager
        postAdapter = PostAdapter(postList,this)
        postRecyclerView.adapter = postAdapter
    }

    fun addPost(view: View) {
        val intent = Intent(this, ShareActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFirebaseData() {
        db.collection("Posts").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        postList.clear()
                        for (document in documents) {
                            val userName = document.get("userName") as String?
                            val postDescribe = document.get("postDescribe") as String?
                            val imageUrl = document.get("downloadUrl") as String?
                            var post = Post(userName, postDescribe, imageUrl)
                            postList.add(post)

                        }
                        postAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}