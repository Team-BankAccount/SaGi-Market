package com.bankaccount.sagi_market

import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityMainBinding
import com.bankaccount.sagi_market.databinding.ActivityMypostModifyBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import post.PostModel
import util.FirebaseAuth
import util.FirebaseRef

class MypostModifyActivity : BaseActivity<ActivityMypostModifyBinding>(R.layout.activity_mypost_modify) {

    override fun viewSetting() {
        val key = intent.getStringExtra("key").toString()
        Toast.makeText(this,key,Toast.LENGTH_LONG).show()

        firebaseGetData(key)
        firebaseGetImgData(key)

        binding.btnSuccess.setOnClickListener {
            modifyPost(key)
            finish()
        }
    }
    private fun modifyPost(key: String){
        FirebaseRef.postRef
            .child(key)
            .setValue(
                PostModel(binding.textTitle.text.toString(),
                    binding.textPrice.text.toString(),
                    binding.textDetail.text.toString(),
                    FirebaseAuth.getUid(),
                    FirebaseAuth.getTime())
            )
    }
    fun firebaseGetData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(PostModel::class.java)
                if(dataModel != null){
                    binding.textTitle.setText(dataModel!!.title)
                    binding.textDetail.setText(dataModel!!.detail)
                    binding.textPrice.setText(dataModel!!.price)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FirebaseRef.postRef.child(key).addValueEventListener(postListener)
    }
    fun firebaseGetImgData(key : String){

        val storageReference = Firebase.storage.reference.child(key+".png")

        val imageView = binding.imgPost

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }else{

            }
        })

    }
}