package kr.co.drgem.managingapp.menu.request.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.request.activity.RequestDetailActivity

class RequestListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.request_list_item, parent, false)
) {

    fun bind(){

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, RequestDetailActivity::class.java)
            itemView.context.startActivity(myIntent)
        }

    }

}