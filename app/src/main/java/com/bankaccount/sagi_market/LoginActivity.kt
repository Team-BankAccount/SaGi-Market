package com.bankaccount.sagi_market

import android.content.Intent
import android.util.Log
import android.view.View
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityLoginBinding
import com.bankaccount.sagi_market.preference.MySharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun viewSetting() {
        binding.login = this

        auth = Firebase.auth


        binding.btnLogin.setOnClickListener {
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()
            if(email.isEmpty()) {
                shortToast("이메일을 입력해 주세요")
            } else if(password.isEmpty()){
                shortToast("비밀번호를 입력해 주세요")
            } else{
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String){
        val userId = email.split("@")[0]
        val currentUserDB = Firebase.database.getReference("user").child(userId)
        val name = currentUserDB.child("name").toString()



        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    MySharedPreferences.setUserEmail(this, email)
                    MySharedPreferences.setUserPass(this, password)
                    MySharedPreferences.setUserName(this, name)
                    isFirstLogin(userId)
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    shortToast("로그인에 실패했습니다.")
                }
            }
    }

    private fun isFirstLogin(userId: String){
        val currentUserDB = Firebase.database.getReference("user").child(userId)
        val uid = auth.currentUser!!.uid
        currentUserDB.child("uid").setValue(uid)
    }


    fun onClickPageBtn(view: View){
        lateinit var act: Class<*>
        when(view.id) {
            binding.tvRegister.id -> {
                act = RegisterActivity::class.java
            }
            binding.tvFindPassword.id -> {
                act = FindPasswordActivity::class.java
            }
        }
        val intent = Intent(this, act)
        startActivity(intent)
        finish()
    }

}