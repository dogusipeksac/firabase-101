package com.example.dusuncepaylas.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dusuncepaylas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_share.*
import java.util.*
import java.util.jar.Manifest


class ShareActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    var chosenImage: Uri? = null
    var chosenBitmap: Bitmap? = null
    private val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        auth = Firebase.auth
    }

    fun shareButton(view: View) {
        if (chosenImage != null) {
            val reference = storage.reference
            val uuid = UUID.randomUUID()
            var imageName = "${uuid}.jpg"
            val imageReference = reference.child("images").child(imageName)
            imageReference.putFile(chosenImage!!).addOnSuccessListener { task ->
                val downloadedImageReference = reference.child("images").child(imageName)
                downloadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadedUrl = uri.toString()
                    saveDatabase(downloadedUrl)
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            saveDatabase(null)
        }


    }

    private fun saveDatabase(downloadedUrl:String?) {

        val postDescribe = share_text.text.toString()
        val userName = auth.currentUser!!.displayName.toString()
        val date = com.google.firebase.Timestamp.now()

        val shareMap = hashMapOf<String, Any>()
        shareMap["postDescribe"] = postDescribe
        shareMap["userName"] = userName
        shareMap["date"] = date
        if (downloadedUrl!=null){
            shareMap["downloadUrl"] = downloadedUrl
        }
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

    fun chooseImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //izin verilmemiş
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )

        } else {
            //izin zaten verilmiş , direk galeriye gidebilirz
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //izin zaten verilmiş , direk galeriye gidebilirz
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            chosenImage = data.data
            sharing_imageview.visibility = View.VISIBLE
            if (chosenImage != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, chosenImage!!)
                    chosenBitmap = ImageDecoder.decodeBitmap(source)
                    sharing_imageview.setImageBitmap(chosenBitmap)

                } else {
                    // eski telefonlar için
                    chosenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, chosenImage)
                    sharing_imageview.setImageBitmap(chosenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}