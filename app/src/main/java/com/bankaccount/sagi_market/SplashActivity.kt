package com.bankaccount.sagi_market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bankaccount.sagi_market.preference.MySharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(MySharedPreferences.getUserEmail(this).isNullOrBlank()||MySharedPreferences.getUserPass(this).isNullOrBlank()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        else {
            login(MySharedPreferences.getUserEmail(this), MySharedPreferences.getUserPass(this))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }



    }

    private fun login(email: String, password: String){
        Firebase.auth.signInWithEmailAndPassword(email, password)
        Toast.makeText(this, "${MySharedPreferences.getUserName(this)}님 로그인 되었습니다", Toast.LENGTH_SHORT).show()
    }

}