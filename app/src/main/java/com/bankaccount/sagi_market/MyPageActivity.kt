package com.bankaccount.sagi_market


import android.content.Intent
import android.util.Log
import android.view.View
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityMypageBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import util.FirebaseRef.Companion.userRef

class MyPageActivity : BaseActivity<ActivityMypageBinding>(R.layout.activity_mypage) {

    private lateinit var email: String
    private lateinit var userId: String
    private lateinit var userUid: String
    private lateinit var userName: String
    private val auth = Firebase.auth

    override fun viewSetting() {
        profileImgSetting()
        nameSetting()
//        binding.tvSell.setOnClickListener{
//            val intent = Intent(this, HaveSellActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun profileImgSetting(){
        email = auth.currentUser?.email.toString()
        userUid = auth.currentUser?.uid.toString()
        userId = email.split("@")[0]
        val profileImg = Firebase.storage.getReference("profileImg").child("$userId.png")
        profileImg.downloadUrl.addOnSuccessListener { Uri ->
            val imageUrl = Uri.toString()
            Glide.with(this).load(imageUrl).into(binding.imgProfile)
        }
    }

    private fun nameSetting(){
        userRef.child(userId).child("name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.tvUserName.text = userName
    }

    fun onClickPageBtn(view: View){
        lateinit var act: Class<*>
        when(view.id){
            binding.btnSell.id, binding.tvSell.id -> {
                act = HaveSellActivity::class.java
            }
        }
        val intent = Intent(this, act)
        startActivity(intent)
    }
}