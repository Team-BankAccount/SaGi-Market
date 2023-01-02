package post

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bankaccount.sagi_market.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import util.FirebaseRef


class PostAdapter(private val postList : MutableList<PostModel>) : BaseAdapter(){

    val postKeyList = mutableListOf<String>()

    override fun getCount(): Int {
        return postList.size
    }

    override fun getItem(p0: Int): Any {
       return postList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var p1 = p1
        if(p1 == null){
            p1 = LayoutInflater.from(p2?.context).inflate(R.layout.item_list,p2,false )
        }
        firebaseGetData()

        val title = p1?.findViewById<TextView>(R.id.text_title)
        val price = p1?.findViewById<TextView>(R.id.text_price)
        val time = p1?.findViewById<TextView>(R.id.text_time)
        val img = p1?.findViewById<ImageView>(R.id.img_post)

        title!!.text = postList[p0].title
        price!!.text = postList[p0].price+"₩"
        time!!.text = postList[p0].time
        //img!!.setImageURI(postKeyList[p0].toUri())



        //Log.d(this,postList[p0])
        /*val resourceId = context.resources.getIdentifier(postList[p0], "drawable", context.packageName)
        img!!.setImageResource(resourceId)*/
       // MainActivity().firebaseGetImgData()




        return p1!!
    }
    fun firebaseGetData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(dataModel in snapshot.children){


                    val post = dataModel.getValue(PostModel::class.java)
                    postKeyList.add(dataModel.key.toString())
                }
                postKeyList.reverse()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("abc","loadPost:onCancelled",error.toException())
            }
        }
        FirebaseRef.postRef.addValueEventListener(postListener)
    }

}