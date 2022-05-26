package kr.co.drgem.managingapp.menu.order.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.BaljuData

class OrderListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
) {

    fun bind(){

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, OrderDetailDetailActivity::class.java)
            itemView.context.startActivity(myIntent)
        }

    }

}