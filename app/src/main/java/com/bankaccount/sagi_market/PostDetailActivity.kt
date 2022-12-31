package com.bankaccount.sagi_market
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bankaccount.sagi_market.base.BaseActivity
import com.bankaccount.sagi_market.databinding.ActivityPostDetailBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import post.PostModel
import util.FirebaseRef


class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(R.layout.activity_post_detail){
    private val TAG = PostDetailActivity::class.java.simpleName
    override fun viewSetting() {
        val key = intent.getStringExtra("key")
        Toast.makeText(this,key,Toast.LENGTH_LONG).show()

        firebaseGetData(key.toString())
        firebaseGetImgData(key.toString())
    }
    private fun firebaseGetData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(PostModel::class.java)
                Log.d(TAG,snapshot.toString())
                if(dataModel != null){
                    binding.textTitle.text = dataModel!!.title
                    binding.textDetail.text = dataModel!!.detail
                    binding.textPrice.text = dataModel!!.price+"₩"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"loadPost:onCancelled",error.toException())
            }
        }
        FirebaseRef.postRef.child(key).addValueEventListener(postListener)
    }
    fun firebaseGetImgData(key : String){

        val storageReference = Firebase.storage.reference.child(key+".png")

        val imageView = binding.imgPost

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }else{

            }
        })

    }
}

