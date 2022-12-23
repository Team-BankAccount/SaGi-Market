package com.bankaccount.sagi_market

import Post.PostModel
import Util.FirebaseAuth
import Util.FirebaseRef
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityPostBinding
import com.bankaccount.sagi_market.databinding.ActivityRegisterBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostActivity : BaseActivity<ActivityPostBinding>(R.layout.activity_post) {

    override fun viewSetting() {
        binding.btnSuccess.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val price = binding.etPrice.text.toString()
            val detail = binding.etDetail.text.toString()
            val uid = FirebaseAuth.getUid()
            val time = FirebaseAuth.getTime()

            FirebaseRef.postRef
                .push()
                .setValue(PostModel(title,price,detail,uid,time))
            Toast.makeText(this,"게시글 업로드 완료",Toast.LENGTH_SHORT).show()
            imgUpload()
            finish()
        }
        binding.btnImg.setOnClickListener {
            ActivityCompat.requestPermissions(this@PostActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            if (ContextCompat.checkSelfPermission(this@PostActivity.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery,100)

            } else {
                Toast.makeText(this,"갤러리 접근이 거부 되었습니다",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun imgUpload(){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("mountains.jpg")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 100){

        }
    }
}