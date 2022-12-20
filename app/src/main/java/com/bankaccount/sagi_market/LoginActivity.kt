package com.bankaccount.sagi_market

import android.widget.Toast
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<LoginActivityBinding>(R.layout.login_activity) {

    private lateinit var auth: FirebaseAuth

    override fun viewSetting() {

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

}