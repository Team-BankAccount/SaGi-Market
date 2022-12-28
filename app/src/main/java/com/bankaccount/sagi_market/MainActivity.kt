package com.bankaccount.sagi_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bankaccount.sagi_market.R
import android.widget.Button
import android.widget.Toast
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun viewSetting() {
        binding.btnPost.setOnClickListener {
            var intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }
    }
}