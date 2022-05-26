package kr.co.drgem.managingapp.menu.order.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.viewholder.OrderListViewHolder
import kr.co.drgem.managingapp.models.BaljuData

class OrderListAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is OrderListViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = 40
}