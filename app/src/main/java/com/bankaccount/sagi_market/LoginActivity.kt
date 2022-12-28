package com.bankaccount.sagi_market

import android.content.Intent
import android.view.View
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var auth: FirebaseAuth

    override fun viewSetting() {
        binding.login = this

        auth = Firebase.auth
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        binding.btnLogin.setOnClickListener {
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

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    shortToast("로그인에 실패했습니다.")
                }
            }
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