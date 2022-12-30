package com.bankaccount.sagi_market

import Post.PostAdapter
import Post.PostModel
import Util.FirebaseRef
import android.content.Intent
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
    val postKeyList = mutableListOf<String>()

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

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this,PostDetailActivity::class.java)
            intent.putExtra("key",postKeyList[i])
            startActivity(intent)
        }
    }
    private fun firebaseGetData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postDataList.clear()

                for(dataModel in snapshot.children){

                    Log.d(TAG,dataModel.toString())
                    val post = dataModel.getValue(PostModel::class.java)
                    postDataList.add(post!!)
                    postKeyList.add(dataModel.key.toString())
                }
                postDataList.reverse()
                postKeyList.reverse()
                postadapter.notifyDataSetChanged()
                Log.d(TAG,postDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"loadPost:onCancelled",error.toException())
            }
        }
        FirebaseRef.postRef.addValueEventListener(postListener)
    }
}
