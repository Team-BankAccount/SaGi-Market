package com.bankaccount.sagi_market

import post.PostAdapter
import post.PostModel
import util.FirebaseRef
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bankaccount.sagi_market.R
import android.widget.Button
import android.widget.Toast
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityMainBinding
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

   // val a = FirebaseRef.postRef



    val postDataList = mutableListOf<PostModel>()
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var postadapter : PostAdapter

    override fun viewSetting() {
        //println("안"+a.toString())

        binding.btnPost.setOnClickListener {
            var intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        postadapter = PostAdapter(postDataList)
        binding.listView.adapter = postadapter

        firebaseGetData()




    }
    private fun firebaseGetData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postDataList.clear()

                for(dataModel in snapshot.children){

                    Log.d(TAG,dataModel.toString())
                    val post = dataModel.getValue(PostModel::class.java)
                    postDataList.add(post!!)
                }
                postadapter.notifyDataSetChanged()
                Log.d(TAG,postDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"loadPost:onCancelled",error.toException())
            }
        }
        FirebaseRef.postRef.addValueEventListener(postListener)
    }
    fun firebaseGetImgData(key : String){

        val storageReference = Firebase.storage.reference.child(key+"png")

        val imageView = findViewById<ImageView>(R.id.img_post)

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener {task ->
            if(task.isSuccessful) {
                Glide.with(this /* context */)
                    .load(task.result)
                    .into(imageView)
            }else{

            }
        })
    }

}
