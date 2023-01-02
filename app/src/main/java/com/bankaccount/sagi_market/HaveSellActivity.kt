package com.bankaccount.sagi_market

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityHaveSalepageBinding
import com.bankaccount.sagi_market.databinding.ItemLlistSalepageBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import post.MyPostModel
import post.PostModel

class HaveSellActivity : BaseActivity<ActivityHaveSalepageBinding>(R.layout.activity_have_salepage) {

    private var post: ArrayList<MyPostModel> = arrayListOf()
    val myUid = Firebase.auth.currentUser?.uid.toString()

    override fun viewSetting() {
        binding.rcMyPost.layoutManager = LinearLayoutManager(this)
        binding.rcMyPost.adapter = MyPostAdapter()
    }



    inner class MyPostAdapter : RecyclerView.Adapter<MyPostAdapter.CustomViewHolder>(){
        var dataList = mutableListOf<MyPostModel>()

        init {
            FirebaseDatabase.getInstance().reference.child("post").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    post.clear()
                    for(data in snapshot.children){
                        val item = data.getValue<MyPostModel>()
                        if(item?.uid.toString() != myUid){ continue }
                        post.add(item!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemLlistSalepageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(private val binding: ItemLlistSalepageBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(postData: MyPostModel){
                binding.tvPrice.text =postData.price
                binding.tvDate.text = postData.time
                binding.tvTitle.text = postData.title
                Glide.with(itemView).load(getImgUrl(myUid)).into(binding.ivImage)
            }
        }

        fun getImgUrl(uid: String): String{
            lateinit var imageUrl: String
            val url = Firebase.storage.reference.child("$uid.png")
            url.downloadUrl.addOnSuccessListener { Uri ->
                imageUrl = Uri.toString()
            }
            return imageUrl
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.bind(dataList[position])
        }

        override fun getItemCount(): Int  = dataList.size
    }
}