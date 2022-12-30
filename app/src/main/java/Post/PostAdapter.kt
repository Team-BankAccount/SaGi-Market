package Post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.inflate
import com.bankaccount.sagi_market.MainActivity
import com.bankaccount.sagi_market.R
import com.bumptech.glide.Glide
import com.google.firebase.database.core.Context
import kotlinx.coroutines.NonDisposableHandle.parent



class PostAdapter(private val postList : MutableList<PostModel>) : BaseAdapter(){
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


        val title = p1?.findViewById<TextView>(R.id.text_title)
        val price = p1?.findViewById<TextView>(R.id.text_price)
        val time = p1?.findViewById<TextView>(R.id.text_time)
        val img = p1?.findViewById<ImageView>(R.id.img_post)

        title!!.text = postList[p0].title
        price!!.text = postList[p0].price+"₩"
        time!!.text = postList[p0].time

        //Log.d(this,postList[p0])
        /*val resourceId = context.resources.getIdentifier(postList[p0], "drawable", context.packageName)
        img!!.setImageResource(resourceId)*/
       // MainActivity().firebaseGetImgData()




        return p1!!
    }

}