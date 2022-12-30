package Post

import android.icu.text.CaseMap.Title

data class PostModel (
    val title : String = "",
    val price : String = "",
    val detail : String= "",
    val uid : String= "",
    val time : String= "",
    val sale : Boolean = true
)
