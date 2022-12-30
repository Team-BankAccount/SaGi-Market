package com.bankaccount.sagi_market

import Post.PostModel
import Util.FirebaseRef
import android.util.Log
import android.widget.ImageView
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityPostDetailBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(R.layout.activity_post_detail){
    private val TAG = PostDetailActivity::class.java.simpleName
    override fun viewSetting() {
        val key = intent.getStringExtra("key")


    }
    fun firebaseGetData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(PostModel::class.java)

                binding.textTitle.text = dataModel!!.title
                binding.textPrice.text = dataModel!!.price
                binding.textDetail.text = dataModel!!.detail
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"loadPost:onCancelled",error.toException())
            }
        }
        FirebaseRef.postRef.addValueEventListener(postListener)
    }
    /*fun firebaseGetImgData(key : String){

        val storageReference = Firebase.storage.reference.child(key+"png")

        val imageView = findViewById<ImageView>(R.id.img_post)

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener {task ->
            if(task.isSuccessful) {
                Glide.with(this *//* context *//*)
                    .load(task.result)
                    .into(imageView)
            }else{

            }
        })
    }*/
}

