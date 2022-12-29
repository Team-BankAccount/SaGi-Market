package com.bankaccount.sagi_market

import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityFindPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class FindPasswordActivity : BaseActivity<ActivityFindPasswordBinding>(R.layout.activity_find_password) {

    private lateinit var auth: FirebaseAuth
    private var FLAG_VERIFY = false
    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    private lateinit var email: String

    override fun viewSetting() {
        lateinit var userId: String
        binding.btnSendEmail.setOnClickListener {
            email = binding.etEmail.text.toString()
            userId = email.split("@")[0]
            if (FLAG_VERIFY) {
                longToast("이미 인증이 완료되었습니다")
            } else {
                emailCheck(email, userId)
            }
        }
        auth = FirebaseAuth.getInstance()
        binding.btnConfirm.setOnClickListener {
            if(FLAG_VERIFY) {
                confirmPasswordChange(email, userId)
            } else{
                shortToast("이메일 인증을 진행해 주세요")
            }
        }
    }

    private fun emailCheck(email: String, userId: String){
        val currentUserDB = Firebase.database.getReference("user").child(userId).child("email")
        currentUserDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val unChangedEmail = snapshot.value.toString()
                if(unChangedEmail == email){
                    shortToast("인증이 완료되었습니다")
                    FLAG_VERIFY = true
                    binding.btnSendEmail.setTextColor(Color.parseColor("#7D7B7B"))
                } else {
                    shortToast("인증이 실패하였습니다.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("onCancelledError", "Failed to read value", error.toException())
            }
        })

    }

    private fun confirmPasswordChange(email: String, userId: String) {
        login(email, userId)
        val newPassword: String = binding.etPassword.text.toString()
        val checkPassword: String = binding.etPasswordCheck.text.toString()
        if(isValidPassword(newPassword, checkPassword) && passwordCheck(newPassword, checkPassword)){
            passwordChange(newPassword, userId)
        }
    }

    private fun login(email: String, userId: String){
        val password = getPassword(userId)
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
    }

    private fun getPassword(userId: String): String{
        val currentUserDB = Firebase.database.getReference("user").child(userId)
        return currentUserDB.child("password").toString()
    }

    private fun isValidPassword(newPassword: String, checkPassword: String): Boolean{
        return if (newPassword.isEmpty()){
            shortToast("비밀번호를 입력해 주세요")
            false
        } else if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            shortToast("비밀번호 형식을 맞춰 주세요")
            false
        }else if (checkPassword.isEmpty()){
            shortToast("비밀번호를 확인해 주세요")
            false
        } else{
            true
        }
    }

    private fun passwordCheck(newPassword: String, checkPassword: String): Boolean{
        return if (newPassword == checkPassword){
            true
        } else{
            shortToast("비밀번호를 확인해 주세요")
            false
        }
    }

    private fun passwordChange(newPassword: String, userId: String){
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.updatePassword(newPassword)
        val currentUserDB = Firebase.database.getReference("user").child(userId)
        currentUserDB.child("password").setValue(newPassword)
        shortToast("비밀번호 변경완료")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        auth.signOut()
        finish()
    }

}