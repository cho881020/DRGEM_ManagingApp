package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.activity.KittingDetailActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity

class KittingListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.kitting_list_item, parent, false)
) {



    fun bind(){

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, KittingDetailActivity::class.java)
            itemView.context.startActivity(myIntent)
        }


    }

}