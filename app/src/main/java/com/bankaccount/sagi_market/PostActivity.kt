package com.bankaccount.sagi_market

import post.PostModel
import util.FirebaseAuth
import util.FirebaseRef
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityPostBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class PostActivity : BaseActivity<ActivityPostBinding>(R.layout.activity_post) {

    override fun viewSetting() {

        binding.btnSuccess.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val price = binding.etPrice.text.toString()
            val detail = binding.etDetail.text.toString()
            val uid = FirebaseAuth.getUid()
            val time = FirebaseAuth.getTime()

            val firebaseKey = FirebaseRef.postRef.push().key.toString()

            FirebaseRef.postRef
                .child(firebaseKey)
                .setValue(PostModel(title,price,detail,uid,time))
            Toast.makeText(this,"게시글 업로드 완료",Toast.LENGTH_SHORT).show()

            imgUpload(firebaseKey)

            finish()
        }
        binding.btnImg.setOnClickListener {
            /*ActivityCompat.requestPermissions(this@PostActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            if (ContextCompat.checkSelfPermission(this@PostActivity.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery,100)

            } else {
                Toast.makeText(this,"갤러리 접근이 거부 되었습니다",Toast.LENGTH_SHORT).show()
            }*/

            requestPermission()
        }
        binding.btnHome.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun imgUpload(firebaseKey : String){
        // Get the data from an ImageView as bytes
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(firebaseKey+".png")


        val imageView = binding.btnImg
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 1004){
            binding.btnImg.setImageURI(data?.data)
        }
    }

    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun requestPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }
        }else{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,1004)            //권한 허용
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSIONS_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery,1004)
                    //권한 허용
                }else{
                    //권한 거부됨
                }
                return
            }
        }
    }
}