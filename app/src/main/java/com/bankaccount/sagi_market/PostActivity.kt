package com.bankaccount.sagi_market

import Post.PostModel
import Util.FirebaseAuth
import Util.FirebaseRef
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityPostBinding
import com.bankaccount.sagi_market.databinding.ActivityRegisterBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
            finish()

        }
    }
}