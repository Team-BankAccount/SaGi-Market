package com.bankaccount.sagi_market

import android.content.Intent
import android.util.Patterns
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    private lateinit var auth: FirebaseAuth
    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

    override fun viewSetting() {
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(isValidEmail(email) && isValidPassword(password) && passwordCheck(password)){
            createUser(email, password)
        }
    }

    private fun isValidEmail(email: String): Boolean{
        return if(email.isEmpty()){
            shortToast("이메일을 입력해 주세요")
            false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            shortToast("이메일 형식이 맞지않습니다.")
            false
        } else {
            true
        }
    }

    private fun isValidPassword(password: String): Boolean{
        return if(password.isEmpty()){
            shortToast("비밀번호를 입력해 주세요")
            false
        } else if(!PASSWORD_PATTERN.matcher(password).matches()){
            shortToast("비밀번호 형식을 맞춰주세요")
            false
        } else {
            true
        }
    }

    private fun passwordCheck(password: String): Boolean{
        return if(password == binding.etPasswordCheck.text.toString()){
            true
        } else{
            shortToast("비밀번호를 확인해주세요")
            false
        }
    }

    private fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                    result ->
                if(result.isSuccessful){
                    shortToast("회원가입이 되었습니다.")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    shortToast("화원가입 실패")
                }
            }
    }
}