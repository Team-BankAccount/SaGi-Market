package com.bankaccount.sagi_market

import android.content.Intent
import android.view.View
import android.widget.Toast
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

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Login Failled",Toast.LENGTH_SHORT).show()
                    }
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

    }

}