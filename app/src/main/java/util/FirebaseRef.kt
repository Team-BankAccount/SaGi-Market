package util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef{
    companion object{
        private val database = Firebase.database

        val postRef = database.getReference("post")
        val userRef = database.getReference("user")
    }
}