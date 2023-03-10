package com.bankaccount.sagi_market

import android.content.Intent
import android.util.Patterns
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(isValidEmail(email) && isValidPassword(password) && passwordCheck(password)){
            createUser(email, password, name)
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

    private fun createUser(email: String, password: String, name: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                    result ->
                if(result.isSuccessful){
                    shortToast("회원가입이 되었습니다.")
                    saveUserData(email, password, name)
                }
                else{
                    shortToast("화원가입 실패")
                }
            }
    }

    private fun saveUserData(email: String, password: String, name: String){

        val emailId = email.split("@")[0]
        val currentUserDB = Firebase.database.getReference("user")
        val user = mutableMapOf<String?, Any>(emailId to "")
        currentUserDB.updateChildren(user)
        val userEmail = mutableMapOf<String, Any>()
        val userName = mutableMapOf<String, Any>()
        val userPassword = mutableMapOf<String, Any>()
        val userUid = mutableMapOf<String, Any>()
        userEmail["email"] = email
        userName["name"] = name
        userPassword["password"] = password
        userUid["uid"] = "baseUid"
        currentUserDB.child(emailId).updateChildren(userEmail)
        currentUserDB.child(emailId).updateChildren(userName)
        currentUserDB.child(emailId).updateChildren(userPassword)
        currentUserDB.child(emailId).updateChildren(userUid)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}